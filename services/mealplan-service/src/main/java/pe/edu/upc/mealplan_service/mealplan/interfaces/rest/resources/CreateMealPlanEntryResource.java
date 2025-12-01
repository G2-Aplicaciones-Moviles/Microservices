package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

public record CreateMealPlanEntryResource(
        int recipeId,
        String type,
        int day
) {
}
