package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource;

/**
 * DTO for recipe nutrition information from recipes-service.
 *
 * @param calories the total calories
 * @param carbs    the carbohydrates in grams
 * @param proteins the proteins in grams
 * @param fats     the fats in grams
 */
public record RecipeNutritionResource(
        double calories,
        double carbs,
        double proteins,
        double fats
) {
}
