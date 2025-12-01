package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

public record MealPlanEntryResource(
        int id,
        int recipeId,
        int day,
        int mealPlanType,
        int mealPlanId
) {
}
