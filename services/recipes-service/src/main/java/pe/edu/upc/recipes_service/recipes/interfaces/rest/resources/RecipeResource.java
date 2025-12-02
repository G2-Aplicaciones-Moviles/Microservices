package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

import java.util.List;

public record RecipeResource(
        Integer id,
        // 🆕 Nuevos campos que reemplazan a 'userId' para claridad
        Long createdByNutritionistId, // ID del nutricionista que creó la plantilla (null si es personal)
        Integer assignedToProfileId,   // ID del perfil al que está asignada (null si es plantilla)
        String name,
        String description,
        int preparationTime,
        String difficulty,
        String categoryName,
        String recipeTypeName,
        List<RecipeIngredientResource> ingredients
) {}


