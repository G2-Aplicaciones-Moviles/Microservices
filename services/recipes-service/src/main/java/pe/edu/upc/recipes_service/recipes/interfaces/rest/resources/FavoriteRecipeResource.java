package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

public record FavoriteRecipeResource(Long id, Long userId, int recipeId, String recipeName) {
}
