package pe.edu.upc.tracking_service.tracking.domain.model.commands;

import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.GoalTypes;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

public record UpdateTrackingGoalCommand(
        UserId userId,
        GoalTypes goalType
) {
}