package pe.edu.upc.tracking_service.tracking.domain.model.commands;

import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MealPlanType;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.RecipeId;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

public record CreateMealPlanEntryToTrackingCommand(UserId userId, Long TrackingId, RecipeId recipeId, MealPlanType mealPlanType, int DayNumber) {
}
