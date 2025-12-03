package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.FavoriteRecipeCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.FavoriteRecipeQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteFavoriteRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllFavoriteRecipesByUserIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetFavoriteRecipeByUserIdAndRecipeIdQuery;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateFavoriteRecipeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.FavoriteRecipeResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CreateFavoriteRecipeCommandFromResourceAssembler;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.FavoriteRecipeResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/favorite-recipes", produces = "application/json")
@Tag(name = "FavoriteRecipes", description = "Favorite Recipes Management Endpoints")
public class FavoriteRecipesController {

    private final FavoriteRecipeCommandServiceImpl favoriteRecipeCommandService;
    private final FavoriteRecipeQueryServiceImpl favoriteRecipeQueryService;

    public FavoriteRecipesController(FavoriteRecipeCommandServiceImpl favoriteRecipeCommandService, FavoriteRecipeQueryServiceImpl favoriteRecipeQueryService) {
        this.favoriteRecipeCommandService = favoriteRecipeCommandService;
        this.favoriteRecipeQueryService = favoriteRecipeQueryService;
    }

    @PostMapping
    public ResponseEntity<FavoriteRecipeResource> createFavoriteRecipe(@RequestBody CreateFavoriteRecipeResource resource) {
        var createFavoriteRecipeCommand = CreateFavoriteRecipeCommandFromResourceAssembler.toCommandFromResource(resource);

        var favoriteRecipeId = this.favoriteRecipeCommandService.handle(createFavoriteRecipeCommand);

        var query = new GetFavoriteRecipeByUserIdAndRecipeIdQuery(resource.userId(), resource.recipeId());
        var optionalFavorite = this.favoriteRecipeQueryService.handle(query);

        if (optionalFavorite.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var favoriteResource = FavoriteRecipeResourceFromEntityAssembler.toResource(optionalFavorite.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteResource);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteRecipeResource>> getFavoriteRecipesByUserId(@PathVariable Long userId) {
        var query = new GetAllFavoriteRecipesByUserIdQuery(userId);
        var favorites = this.favoriteRecipeQueryService.handle(query);

        var favoriteResources = favorites.stream()
                .map(FavoriteRecipeResourceFromEntityAssembler::toResource)
                .toList();

        return ResponseEntity.ok(favoriteResources);
    }

    @DeleteMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> deleteFavoriteRecipe(@PathVariable Long userId, @PathVariable int recipeId) {
        var deleteFavoriteRecipeCommand = new DeleteFavoriteRecipeCommand(userId, recipeId);
        this.favoriteRecipeCommandService.handle(deleteFavoriteRecipeCommand);
        return ResponseEntity.noContent().build();
    }
}
