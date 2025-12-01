package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource;

/**
 * DTO for tracking information from tracking-service.
 *
 * @param id                      the tracking ID
 * @param userId                  the user ID
 * @param consumedMacrosId        the consumed macronutrients ID
 * @param trackingGoalId          the tracking goal ID
 */
public record TrackingResource(
        Long id,
        Long userId,
        Long consumedMacrosId,
        Long trackingGoalId
) {
}

