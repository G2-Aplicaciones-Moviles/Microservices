package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

import java.util.List;

public record RecipeTemplateResource(
        Integer id,
        String name,
        String description,
        String category,
        Long createdByNutritionistId,
        String nutritionistName,
        Integer preparationTime,
        String difficulty,
        List<RecipeIngredientResource> ingredients
) {
}
