package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

public record MealPlanTagResource(
        int id,
        String tag,
        int mealPlanId
) {
}
