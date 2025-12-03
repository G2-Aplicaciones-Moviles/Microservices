package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

public record MealPlanEntryDetailedResource(
        int id,
        int recipeId,
        String recipeName,
        String recipeDescription,
        int day,
        int mealPlanType,
        int mealPlanId
) {
}
