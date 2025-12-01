package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource;

import java.util.List;

/**
 * DTO for recipe information from recipes-service.
 *
 * @param id              the recipe ID
 * @param userId          the user ID who created the recipe
 * @param name            the recipe name
 * @param description     the recipe description
 * @param preparationTime the preparation time in minutes
 * @param difficulty      the difficulty level
 * @param category        the recipe category
 * @param recipeType      the type of recipe
 * @param ingredients     the list of ingredient names
 */
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
) {
}
