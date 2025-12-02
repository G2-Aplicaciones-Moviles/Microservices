package pe.edu.upc.recipes_service.recipes.domain.services;

import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesTypesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipeTypeByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RecipeTypeQueryService {
    List<RecipeType> handle(GetAllRecipesTypesQuery query);

    Optional<RecipeType> handle(GetRecipeTypeByIdQuery query);
}
