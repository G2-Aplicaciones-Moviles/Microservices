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

    /**
     * Synchronizes a meal plan entry to the user's tracking.
     * This is called when a recipe is added to a meal plan that is assigned to a user.
     *
     * @param profileId the user profile ID
     * @param recipeId the recipe ID
     * @param mealType the meal type (Breakfast, Lunch, Dinner, Snack)
     * @param day the day number (1-7)
     */
    void syncMealPlanEntryToTracking(Long profileId, int recipeId, String mealType, int day);
}
