package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.TrackingIntegrationClient;

/**
 * Service to communicate with tracking-service via Feign Client.
 */
@Service
public class ExternalTrackingService {

    private final TrackingIntegrationClient trackingClient;

    public ExternalTrackingService(TrackingIntegrationClient trackingClient) {
        this.trackingClient = trackingClient;
    }

    /**
     * Creates a tracking goal for a user based on their profile.
     * This is called after a user profile is created during onboarding.
     *
     * @param userId the user ID
     * @return the ID of the created tracking goal, or null if creation failed
     */
    public Long createTrackingGoalForUser(Long userId) {
        try {
            Long trackingGoalId = trackingClient.createTrackingGoalFromProfile(userId);
            System.out.println("TrackingGoal created successfully for userId: " + userId + " -> trackingGoalId: " + trackingGoalId);
            return trackingGoalId;
        } catch (FeignException.NotFound e) {
            System.err.println("Profile not found in tracking-service for userId: " + userId);
            return null;
        } catch (Exception e) {
            System.err.println("Error creating tracking goal for userId " + userId + ": " + e.getMessage());
            return null;
        }
    }
}

