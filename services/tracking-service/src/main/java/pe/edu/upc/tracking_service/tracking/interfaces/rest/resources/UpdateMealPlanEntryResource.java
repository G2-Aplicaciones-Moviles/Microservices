package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

public record UpdateMealPlanEntryResource(
        Long trackingId,
        Long recipeId,
        String mealPlanType,
        int dayNumber
) {}