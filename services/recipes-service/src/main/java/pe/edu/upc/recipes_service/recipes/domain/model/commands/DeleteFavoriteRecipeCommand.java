package pe.edu.upc.recipes_service.recipes.domain.model.commands;

public record DeleteFavoriteRecipeCommand(Long userId, int recipeId) {
}
