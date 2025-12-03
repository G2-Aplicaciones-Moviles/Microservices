package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.ExternalTrackingService;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.TrackingIntegrationClient;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.TrackingResource;

import java.util.Optional;

/**
 * Implementation of ExternalTrackingService that communicates with tracking-service
 * via Feign Client to retrieve tracking information.
 */
@Service
public class ExternalTrackingServiceImpl implements ExternalTrackingService {

    private final TrackingIntegrationClient trackingClient;

    public ExternalTrackingServiceImpl(TrackingIntegrationClient trackingClient) {
        this.trackingClient = trackingClient;
    }

    /**
     * Retrieves tracking information by user ID from the tracking-service.
     *
     * @param userId the user ID to search for
     * @return an Optional containing the tracking if found, empty otherwise
     */
    @Override
    public Optional<TrackingResource> getTrackingByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            return Optional.empty();
        }

        try {
            TrackingResource tracking = trackingClient.getTrackingByUserId(userId);
            return Optional.ofNullable(tracking);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching tracking for user " + userId + ": "
                    + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Checks if tracking exists for a user in the tracking-service.
     *
     * @param userId the user ID
     * @return true if tracking exists, false otherwise
     */
    @Override
    public boolean trackingExistsForUser(Long userId) {
        try {
            trackingClient.getTrackingByUserId(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking tracking existence for user " + userId
                    + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Synchronizes a meal plan entry to the user's tracking.
     * TODO: Implementar cuando el tracking-service tenga el endpoint correspondiente.
     *
     * @param profileId the user profile ID
     * @param recipeId the recipe ID
     * @param mealType the meal type (Breakfast, Lunch, Dinner, Snack)
     * @param day the day number (1-7)
     */
    @Override
    public void syncMealPlanEntryToTracking(Long profileId, int recipeId, String mealType, int day) {
        if (profileId == null || profileId <= 0) {
            System.err.println("Invalid profileId for tracking sync: " + profileId);
            return;
        }

        try {
            // TODO: Implementar llamada al Feign Client cuando esté disponible
            // CreateMealPlanEntryRequest request = new CreateMealPlanEntryRequest(
            //     recipeId, mealType, day
            // );
            // trackingClient.addMealPlanEntryToTracking(profileId, request);

            System.out.println("Tracking sync stub: profileId=" + profileId +
                    ", recipeId=" + recipeId + ", mealType=" + mealType + ", day=" + day);

        } catch (FeignException.NotFound e) {
            System.err.println("Tracking not found for user " + profileId);
        } catch (Exception e) {
            System.err.println("Error syncing meal plan entry to tracking: " + e.getMessage());
        }
    }
}
