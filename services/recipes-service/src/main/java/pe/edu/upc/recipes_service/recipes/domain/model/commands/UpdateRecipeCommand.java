package pe.edu.upc.recipes_service.recipes.domain.model.commands;

public record UpdateRecipeCommand(
        int recipeId,
        String name,
        String description,
        int preparationTime,
        String difficulty,
        Long categoryId,
        Long recipeTypeId
) {
}
