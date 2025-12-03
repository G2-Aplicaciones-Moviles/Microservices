package pe.edu.upc.recipes_service.recipes.domain.services;

import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeTypeCommand;

public interface RecipeTypeCommandService {
    Long handle(CreateRecipeTypeCommand command);
}
