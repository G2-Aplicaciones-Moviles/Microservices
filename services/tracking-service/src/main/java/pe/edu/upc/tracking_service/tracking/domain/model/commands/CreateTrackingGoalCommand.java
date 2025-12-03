package pe.edu.upc.tracking_service.tracking.domain.model.commands;

import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

public record CreateTrackingGoalCommand(UserId profile, MacronutrientValues macronutrientValues) {
}
