package pe.edu.upc.tracking_service.tracking.domain.model.commands;

public record RemoveMealPlanEntryFromTrackingCommand(long TrackingId, long MealPlanEntryId) {
}