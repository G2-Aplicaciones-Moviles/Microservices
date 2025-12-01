package pe.edu.upc.recipes_service.recipes.domain.model.commands;

public record CreateFavoriteRecipeCommand(Long userId, int recipeId) {
}
