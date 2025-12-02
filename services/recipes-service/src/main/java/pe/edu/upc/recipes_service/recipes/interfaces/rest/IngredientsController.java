package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.IngredientCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.IngredientQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteIngredientCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllIngredientsQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetIngredientsByIdQuery;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateIngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.IngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CreateIngredientCommandFromResourceAssembler;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.IngredientResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/ingredients")
@Tag(name = "Ingredients", description = "Ingredient Management Endpoints")
public class IngredientsController {

    private final IngredientCommandServiceImpl ingredientCommandService;
    private final IngredientQueryServiceImpl ingredientQueryService;

    public IngredientsController(IngredientCommandServiceImpl ingredientCommandService, IngredientQueryServiceImpl ingredientQueryService) {
        this.ingredientCommandService = ingredientCommandService;
        this.ingredientQueryService = ingredientQueryService;
    }

    @PostMapping
    public ResponseEntity<IngredientResource> createIngredient(@RequestBody CreateIngredientResource resource) {
        var createIngredientCommand = CreateIngredientCommandFromResourceAssembler.toCommandFromResource(resource);
        var ingredientId = this.ingredientCommandService.handle(createIngredientCommand);

        var getIngredientByIdQuery = new GetIngredientsByIdQuery(ingredientId);
        var optionalIngredient = this.ingredientQueryService.handle(getIngredientByIdQuery);

        if (optionalIngredient.isEmpty())
            return ResponseEntity.notFound().build();

        var ingredientResource = IngredientResourceFromEntityAssembler.toResourceFromEntity(optionalIngredient.get());
        return new ResponseEntity<>(ingredientResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResource>> getAllIngredients() {
        var getAllIngredientsQuery = new GetAllIngredientsQuery();
        var ingredients = this.ingredientQueryService.handle(getAllIngredientsQuery);
        var ingredientResources = IngredientResourceFromEntityAssembler.toResources(ingredients);
        return ResponseEntity.ok(ingredientResources);
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientResource> getIngredientById(@PathVariable int ingredientId) {
        var getIngredientByIdQuery = new GetIngredientsByIdQuery(ingredientId);
        var optionalIngredient = this.ingredientQueryService.handle(getIngredientByIdQuery);

        if (optionalIngredient.isEmpty())
            return ResponseEntity.notFound().build();

        var ingredientResource = IngredientResourceFromEntityAssembler.toResourceFromEntity(optionalIngredient.get());
        return ResponseEntity.ok(ingredientResource);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable int ingredientId) {
        var deleteIngredientCommand = new DeleteIngredientCommand(ingredientId);
        this.ingredientCommandService.handle(deleteIngredientCommand);
        return ResponseEntity.noContent().build();
    }
}
