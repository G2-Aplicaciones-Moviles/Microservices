package pe.edu.upc.tracking_service.tracking.domain.model.commands;

import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

public record CreateTrackingCommand(UserId profile) {}