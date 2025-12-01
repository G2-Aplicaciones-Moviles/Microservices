package pe.edu.upc.tracking_service.tracking.domain.model.commands;

import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.MealPlanTypes;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.RecipeId;

public record UpdateMealPlanEntryInTrackingCommand(long TrackingId, long MealPlanEntryId, RecipeId recipeId, MealPlanTypes mealPlanType, int dayNumber) {
}

