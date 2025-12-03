package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

public record CreateMealPlanEntryResource(
        Long userId,
        Long recipeId,
        String mealPlanType,
        int dayNumber
) {}