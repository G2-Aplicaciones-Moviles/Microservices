package pe.edu.upc.recipes_service.recipes.domain.services;

import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateFavoriteRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteFavoriteRecipeCommand;

public interface FavoriteRecipeCommandService {
    Long handle(CreateFavoriteRecipeCommand command);
    void handle(DeleteFavoriteRecipeCommand command);
}
