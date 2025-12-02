package pe.edu.upc.recipes_service.recipes.domain.model.commands;

public record CreateRecipeCommand(
        String name,
        String description,
        int preparationTime,
        String difficulty,
        Long categoryId,
        Long recipeTypeId,
        Long createdByNutritionistId,
        Integer assignedToProfileId
) {
}