package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.tracking_service.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateMealPlanEntryToTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.RemoveMealPlanEntryFromTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.UpdateMealPlanEntryInTrackingCommand;

import java.util.Optional;

public interface TrackingCommandService {
    Long handle(CreateTrackingCommand command);

    Long handle(CreateMealPlanEntryToTrackingCommand command);

    void handle(RemoveMealPlanEntryFromTrackingCommand command);

    Optional<Tracking> handle(UpdateMealPlanEntryInTrackingCommand command);
}
