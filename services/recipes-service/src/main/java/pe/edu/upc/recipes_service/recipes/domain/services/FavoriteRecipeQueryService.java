package pe.edu.upc.recipes_service.recipes.domain.services;

import pe.edu.upc.recipes_service.recipes.domain.model.entities.FavoriteRecipe;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllFavoriteRecipesByUserIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetFavoriteRecipeByUserIdAndRecipeIdQuery;

import java.util.List;
import java.util.Optional;

public interface FavoriteRecipeQueryService {
    Optional<FavoriteRecipe> handle(GetFavoriteRecipeByUserIdAndRecipeIdQuery query);
    List<FavoriteRecipe> handle(GetAllFavoriteRecipesByUserIdQuery query);
}
