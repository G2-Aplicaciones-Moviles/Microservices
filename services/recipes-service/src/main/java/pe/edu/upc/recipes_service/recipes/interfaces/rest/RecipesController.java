package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.RecipeCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.RecipeQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesByCategoryIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipesByIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.services.RecipeNutritionService;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.AddIngredientToRecipeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateRecipeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeNutritionResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipes")
@Tag(name = "Recipes", description = "Recipe Management Endpoints")
public class RecipesController {

    private final RecipeCommandServiceImpl recipeCommandService;
    private final RecipeQueryServiceImpl recipeQueryService;
    private final RecipeNutritionService recipeNutritionService;

    public RecipesController(RecipeCommandServiceImpl recipeCommandService, RecipeQueryServiceImpl recipeQueryService,
                             RecipeNutritionService recipeNutritionService) {
        this.recipeCommandService = recipeCommandService;
        this.recipeQueryService = recipeQueryService;
        this.recipeNutritionService = recipeNutritionService;
    }

    @PostMapping
    public ResponseEntity<RecipeResource> createRecipe(@RequestBody CreateRecipeResource resource) {
        var createRecipeCommand = CreateRecipeCommandFromResourceAssembler.toCommandFromResource(resource);
        var recipeId = this.recipeCommandService.handle(createRecipeCommand);

        var getRecipeByIdQuery = new GetRecipesByIdQuery(recipeId);
        var optionalRecipe = this.recipeQueryService.handle(getRecipeByIdQuery);

        if (optionalRecipe.isEmpty())
            return ResponseEntity.notFound().build();

        var recipeResource = RecipeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipe.get());
        return new ResponseEntity<>(recipeResource, HttpStatus.CREATED);
    }

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
            return ResponseEntity.badRequest().body(e.getMessage());
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
