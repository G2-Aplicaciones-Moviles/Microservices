package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

import java.util.List;

public record RecipeResource(
        int id,
        Long userId,
        String name,
        String description,
        int preparationTime,
        String difficulty,
        String category,
        String recipeType,
        List<String> ingredients
) {}


