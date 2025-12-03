package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.RecipeCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.ExternalNutritionistService;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.ExternalUserProfileService;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.RecipeQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesByCategoryIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesByProfileIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipesByIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.services.RecipeNutritionService;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.*;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/recipes")
@Tag(name = "Recipes", description = "Recipe Management Endpoints")
public class RecipesController {

    private final RecipeCommandServiceImpl recipeCommandService;
    private final RecipeQueryServiceImpl recipeQueryService;
    private final RecipeNutritionService recipeNutritionService;
    private final ExternalUserProfileService externalService;
    private final ExternalNutritionistService externalNutritionistService;


    public RecipesController(RecipeCommandServiceImpl recipeCommandService, RecipeQueryServiceImpl recipeQueryService,
                             RecipeNutritionService recipeNutritionService, ExternalUserProfileService externalService,
                             ExternalNutritionistService externalNutritionistService) {
        this.recipeCommandService = recipeCommandService;
        this.recipeQueryService = recipeQueryService;
        this.recipeNutritionService = recipeNutritionService;
        this.externalService = externalService;
        this.externalNutritionistService = externalNutritionistService;
    }

    // ------------------------------------------------------------
    // 1) USER CREATES PERSONAL RECIPE
    // ------------------------------------------------------------
    @PostMapping("/users/{userId}")
    @Operation(summary = "Create personal recipe for user")
    public ResponseEntity<RecipeResource> createRecipeForUser(
            @PathVariable Long userId,
            @RequestBody CreateRecipeResource resource) {

        externalService.validateUserExists(userId);

        // createdByNutritionistId = null, assignedToProfileId = userId
        var command = CreateRecipeCommandFromResourceAssembler.toCommandFromResource(
                resource,
                null,
                userId.intValue()
        );

        int recipeId = this.recipeCommandService.handle(command);

        var optionalRecipe = this.recipeQueryService.handle(new GetRecipesByIdQuery(recipeId));
        if (optionalRecipe.isEmpty()) return ResponseEntity.notFound().build();

        var recipeResource = RecipeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipe.get());
        return new ResponseEntity<>(recipeResource, HttpStatus.CREATED);
    }

    // ------------------------------------------------------------
    // 2) NUTRITIONIST CREATES TEMPLATE RECIPE
    // ------------------------------------------------------------
    @PostMapping("/nutritionists/{userId}")
    @Operation(summary = "Nutritionist creates recipe template")
    public ResponseEntity<RecipeResource> createRecipeForNutritionist(
            @PathVariable Long userId,
            @RequestBody CreateRecipeResource resource) {

        externalService.validateUserExists(userId);

        // createdByNutritionistId = userId, assignedToProfileId = null
        var command = CreateRecipeCommandFromResourceAssembler.toCommandFromResource(
                resource,
                userId,
                null
        );

        int recipeId = this.recipeCommandService.handle(command);

        var optionalRecipe = this.recipeQueryService.handle(new GetRecipesByIdQuery(recipeId));
        if (optionalRecipe.isEmpty()) return ResponseEntity.notFound().build();

        var recipeResource = RecipeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipe.get());
        return new ResponseEntity<>(recipeResource, HttpStatus.CREATED);
    }

    // ------------------------------------------------------------
    // 3) LIST TEMPLATES CREATED BY NUTRITIONIST
    // ------------------------------------------------------------
    @GetMapping("/nutritionists/{nutritionistUserId}/templates")
    @Operation(summary = "List recipe templates created by specific nutritionist (Simple)")
    public ResponseEntity<List<RecipeResource>> getRecipeTemplatesByNutritionist(
            @PathVariable Long nutritionistUserId) {

        externalService.validateUserExists(nutritionistUserId);

        var recipes = this.recipeQueryService.handle(new GetAllRecipesQuery())
                .stream()
                .filter(r ->
                        // Filtra por el ID del nutricionista creador
                        r.getCreatedByNutritionistId() != null &&
                                Objects.equals(r.getCreatedByNutritionistId(), nutritionistUserId) &&
                                r.getAssignedToProfileId() == null // Debe ser una plantilla (no asignada)
                )
                .map(RecipeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(recipes);
    }

    // ------------------------------------------------------------
    // 🆕 3b) LIST DETAILED TEMPLATES CREATED BY NUTRITIONIST (NUEVO)
    // ------------------------------------------------------------
    @GetMapping("/nutritionists/{nutritionistUserId}/templates/detailed")
    @Operation(summary = "List DETAILED recipe templates created by specific nutritionist (Author)")
    public ResponseEntity<List<RecipeTemplateResource>> getTemplatesByNutritionistDetailed(
            @PathVariable Long nutritionistUserId) {

        String nutritionistName = externalNutritionistService.getNutritionistNameOrDefault(nutritionistUserId);

        var templates = this.recipeQueryService.handle(new GetAllRecipesQuery())
                .stream()
                .filter(r ->
                        r.getCreatedByNutritionistId() != null &&
                                Objects.equals(r.getCreatedByNutritionistId(), nutritionistUserId) &&
                                r.getAssignedToProfileId() == null
                )
                .map(r -> new RecipeTemplateResource(
                        r.getId().intValue(),
                        r.getName(),
                        r.getDescription(),
                        r.getCategory().getName(),
                        r.getCreatedByNutritionistId(),
                        nutritionistName,
                        r.getPreparationTime(),
                        r.getDifficulty(),
                        RecipeResourceFromEntityAssembler.mapRecipeIngredientsToResources(r.getRecipeIngredients())
                ))
                .toList();

        return ResponseEntity.ok(templates);
    }

    // ------------------------------------------------------------
    // 4) ASSIGN / COPY TEMPLATE TO PROFILE
    // ------------------------------------------------------------
    @PostMapping("/{recipeId}/assign-to-profile/{profileId}")
    @Operation(summary = "Assign/copy recipe template to user profile")
    public ResponseEntity<?> assignRecipeToProfile(
            @PathVariable int recipeId,
            @PathVariable Long profileId) {

        externalService.validateUserExists(profileId);

        var existingOpt = recipeQueryService.handle(new GetRecipesByIdQuery(recipeId));
        if (existingOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Recipe template not found"));

        Recipe existing = existingOpt.get();

        if (existing.getAssignedToProfileId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Cannot assign. Recipe is not a template or is already assigned."));
        }

        // Crear el recurso de copia a partir de la receta existente (simulando CreateRecipeResource)
        CreateRecipeResource copyResource = new CreateRecipeResource(
                existing.getName(),
                existing.getDescription(),
                existing.getPreparationTime(),
                existing.getDifficulty(),
                existing.getCategory().getId(),
                existing.getRecipeType().getId()
        );

        // Crear el comando con la asignación al nuevo perfil, manteniendo el creador original
        var copyCommand = CreateRecipeCommandFromResourceAssembler.toCommandFromResource(
                copyResource,
                existing.getCreatedByNutritionistId(), // createdByNutritionistId = el creador original
                profileId.intValue()                  // assignedToProfileId = el nuevo usuario
        );

        int newRecipeId = this.recipeCommandService.handle(copyCommand);

        var newEntityOpt = recipeQueryService.handle(new GetRecipesByIdQuery(newRecipeId));
        if (newEntityOpt.isEmpty()) return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(
                RecipeResourceFromEntityAssembler.toResourceFromEntity(newEntityOpt.get()),
                HttpStatus.CREATED
        );
    }

    // ------------------------------------------------------------
    // 5) LIST ALL TEMPLATES (Global) (SIMPLE - SIN NOMBRE DE AUTOR)
    // ------------------------------------------------------------
    @GetMapping("/templates")
    @Operation(summary = "Get all recipe templates created by nutritionists (Simple)")
    public ResponseEntity<List<RecipeResource>> getAllTemplates() {
        var templates = this.recipeQueryService.handle(new GetAllRecipesQuery())
                .stream()
                .filter(r ->
                        // Es plantilla si NO tiene assignedToProfileId
                        r.getAssignedToProfileId() == null &&
                                // Y fue creado por un nutricionista
                                r.getCreatedByNutritionistId() != null
                )
                .map(RecipeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(templates);
    }

    // ------------------------------------------------------------
    // 🆕 5b) LIST ALL DETAILED TEMPLATES (NUEVO)
    // ------------------------------------------------------------
    @GetMapping("/templates/detailed")
    @Operation(summary = "Get all recipe templates with nutritionist info (Author)")
    public ResponseEntity<List<RecipeTemplateResource>> getAllTemplatesDetailed() {
        var templates = this.recipeQueryService.handle(new GetAllRecipesQuery())
                .stream()
                .filter(r ->
                        r.getAssignedToProfileId() == null &&
                                r.getCreatedByNutritionistId() != null
                )
                .map(r -> {
                    Long nutritionistId = r.getCreatedByNutritionistId();

                    String nutritionistName = externalNutritionistService.getNutritionistNameOrDefault(nutritionistId);

                    return new RecipeTemplateResource(
                            r.getId().intValue(),
                            r.getName(),
                            r.getDescription(),
                            r.getCategory().getName(),
                            nutritionistId,
                            nutritionistName,
                            r.getPreparationTime(),
                            r.getDifficulty(),
                            RecipeResourceFromEntityAssembler.mapRecipeIngredientsToResources(r.getRecipeIngredients())
                    );
                })
                .toList();

        return ResponseEntity.ok(templates);
    }

    // ------------------------------------------------------------
    // 6) LIST RECIPES ASSIGNED TO A SPECIFIC PROFILE
    // ------------------------------------------------------------
    @GetMapping("/profile/{profileId}")
    @Operation(summary = "Get all recipes assigned to a specific user profile")
    public ResponseEntity<List<RecipeResource>> getRecipesByProfileId(@PathVariable int profileId) {
        var recipes = recipeQueryService.handle(new GetAllRecipesByProfileIdQuery(profileId));

        if (recipes.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        var resources = recipes.stream()
                .map(RecipeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    // ------------------------------------------------------------
    // ENDPOINTS EXISTENTES
    // ------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<RecipeResource>> getAllRecipes() {
        var getAllRecipesQuery = new GetAllRecipesQuery();
        var recipes = this.recipeQueryService.handle(getAllRecipesQuery);
        var recipeResources = RecipeResourceFromEntityAssembler.toResources(recipes);
        return ResponseEntity.ok(recipeResources);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResource> getRecipeById(@PathVariable int recipeId) {
        var getRecipeByIdQuery = new GetRecipesByIdQuery(recipeId);
        var optionalRecipe = this.recipeQueryService.handle(getRecipeByIdQuery);

        if (optionalRecipe.isEmpty())
            return ResponseEntity.notFound().build();

        var recipeResource = RecipeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipe.get());
        return ResponseEntity.ok(recipeResource);
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<RecipeResource>> getAllRecipesByCategory(@PathVariable Long categoryId) {
        var getAllRecipesByCategoryQuery = new GetAllRecipesByCategoryIdQuery(categoryId);
        var recipes = this.recipeQueryService.handle(getAllRecipesByCategoryQuery);
        var recipeResources = RecipeResourceFromEntityAssembler.toResources(recipes);
        return ResponseEntity.ok(recipeResources);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeResource> updateRecipe(@PathVariable int recipeId, @RequestBody CreateRecipeResource resource) {
        var updateRecipeCommand = UpdateRecipeCommandFromResourceAssembler.toCommandFromResource(recipeId, resource);
        var optionalRecipe = this.recipeCommandService.handle(updateRecipeCommand);

        if (optionalRecipe.isEmpty())
            return ResponseEntity.badRequest().build();

        var recipeResource = RecipeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipe.get());
        return ResponseEntity.ok(recipeResource);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable int recipeId) {
        var deleteRecipeCommand = new DeleteRecipeCommand(recipeId);
        this.recipeCommandService.handle(deleteRecipeCommand);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{recipeId}/add-ingredient")
    public ResponseEntity<?> addIngredientToRecipe(
            @PathVariable int recipeId,
            @RequestBody AddIngredientToRecipeResource resource) {

        var command = AddIngredientToRecipeCommandFromResourceAssembler.toCommand(recipeId, resource);

        try {
            this.recipeCommandService.handle(command);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{recipeId}/nutrition")
    public ResponseEntity<RecipeNutritionResource> getRecipeNutrition(@PathVariable int recipeId) {
        var opt = recipeQueryService.handle(new GetRecipesByIdQuery(recipeId));
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var vo = recipeNutritionService.compute(opt.get());
        return ResponseEntity.ok(RecipeNutritionResourceAssembler.toResource(vo));
    }
}
