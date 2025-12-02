package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource;

/**
 * Resource representing a user profile from profiles-service.
 */
public record UserProfileResource(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}

