package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.RecipeTypeCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.RecipeTypeQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesTypesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipeTypeByIdQuery;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateRecipeTypeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeTypeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CreateRecipeTypeCommandFromResourceAssembler;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.RecipeTypeResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipe-types")
@Tag(name = "Recipe Types", description = "Recipe Type Management Endpoints")
public class RecipesTypesController {

    private final RecipeTypeCommandServiceImpl recipeTypeCommandService;
    private final RecipeTypeQueryServiceImpl recipeTypeQueryService;

    public RecipesTypesController(RecipeTypeCommandServiceImpl recipeTypeCommandService, RecipeTypeQueryServiceImpl recipeTypeQueryService) {
        this.recipeTypeCommandService = recipeTypeCommandService;
        this.recipeTypeQueryService = recipeTypeQueryService;
    }

    @PostMapping
    public ResponseEntity<RecipeTypeResource> createRecipeType(@RequestBody CreateRecipeTypeResource resource) {
        var createRecipeTypeCommand = CreateRecipeTypeCommandFromResourceAssembler.toCommandFromResource(resource);
        var recipeTypeId = this.recipeTypeCommandService.handle(createRecipeTypeCommand);

        var recipeType = this.recipeTypeQueryService.handle(new GetRecipeTypeByIdQuery(recipeTypeId));

        if (recipeType.isEmpty())
            return ResponseEntity.notFound().build();

        var recipeTypeResource = RecipeTypeResourceFromEntityAssembler.toResourceFromEntity(recipeType.get());
        return new ResponseEntity<>(recipeTypeResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipeTypeResource>> getAllRecipeTypes() {
        var recipeTypes = this.recipeTypeQueryService.handle(new GetAllRecipesTypesQuery());
        var recipeTypeResources = RecipeTypeResourceFromEntityAssembler.toResources(recipeTypes);
        return ResponseEntity.ok(recipeTypeResources);
    }

    @GetMapping("/{recipeTypeId}")
    public ResponseEntity<RecipeTypeResource> getRecipeTypeById(@PathVariable Long recipeTypeId) {
        var optionalRecipeType = this.recipeTypeQueryService.handle(new GetRecipeTypeByIdQuery(recipeTypeId));

        if (optionalRecipeType.isEmpty())
            return ResponseEntity.notFound().build();

        var recipeTypeResource = RecipeTypeResourceFromEntityAssembler.toResourceFromEntity(optionalRecipeType.get());
        return ResponseEntity.ok(recipeTypeResource);
    }
}
