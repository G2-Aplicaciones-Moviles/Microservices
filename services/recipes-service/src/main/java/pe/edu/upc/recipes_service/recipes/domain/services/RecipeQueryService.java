package pe.edu.upc.recipes_service.recipes.domain.services;

import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesByCategoryIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesByProfileIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipesByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RecipeQueryService {
    List<Recipe> handle(GetAllRecipesQuery query);

    List<Recipe> handle(GetAllRecipesByCategoryIdQuery query);

    Optional<Recipe> handle(GetRecipesByIdQuery query);

    List<Recipe> handle(GetAllRecipesByProfileIdQuery query);
}
