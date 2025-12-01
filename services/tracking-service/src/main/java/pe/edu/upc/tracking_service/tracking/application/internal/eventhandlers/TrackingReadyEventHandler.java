package pe.edu.upc.tracking_service.tracking.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.SeedTrackingMasterDataCommand;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingMasterDataCommandService;

import java.sql.Timestamp;

@Service
public class TrackingReadyEventHandler {

    private final TrackingMasterDataCommandService trackingMasterDataCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingReadyEventHandler.class);

    public TrackingReadyEventHandler(TrackingMasterDataCommandService trackingMasterDataCommandService) {
        this.trackingMasterDataCommandService = trackingMasterDataCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify if tracking master data seeding is needed for {} at {}",
                applicationName, currentTimestamp());

        trackingMasterDataCommandService.handle(new SeedTrackingMasterDataCommand());

        LOGGER.info("Tracking master data seeding verification finished for {} at {}",
                applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}