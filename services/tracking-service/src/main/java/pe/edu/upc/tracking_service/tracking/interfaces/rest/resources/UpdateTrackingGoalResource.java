package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.GoalTypes;

public record UpdateTrackingGoalResource(
        GoalTypes goalType
) {
}