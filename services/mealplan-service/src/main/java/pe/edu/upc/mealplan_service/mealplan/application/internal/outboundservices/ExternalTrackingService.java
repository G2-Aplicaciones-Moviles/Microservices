package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices;

import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.TrackingResource;

import java.util.Optional;

/**
 * Service interface for retrieving tracking information from the tracking-service.
 */
public interface ExternalTrackingService {

    /**
     * Retrieves tracking information by user ID.
     *
     * @param userId the user ID
     * @return an Optional containing the tracking if found, empty otherwise
     */
    Optional<TrackingResource> getTrackingByUserId(Long userId);

    /**
     * Checks if tracking exists for a user.
     *
     * @param userId the user ID
     * @return true if tracking exists, false otherwise
     */
    boolean trackingExistsForUser(Long userId);
}
