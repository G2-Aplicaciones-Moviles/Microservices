package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.tracking_service.tracking.domain.model.commands.SeedTrackingMasterDataCommand;

public interface TrackingMasterDataCommandService {
    void handle(SeedTrackingMasterDataCommand command);
}
