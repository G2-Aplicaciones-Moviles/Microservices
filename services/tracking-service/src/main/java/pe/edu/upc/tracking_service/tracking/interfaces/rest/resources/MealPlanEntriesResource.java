package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

public record MealPlanEntriesResource(Long id, Long recipeId, String mealPlanType, int dayNumber) {
}
