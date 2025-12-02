package pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl.rest.resource;

/**
 * DTO for user profile information from profiles-service.
 *
 * @param id     the user profile ID
 * @param userId the user ID
 * @param gender the gender
 * @param height the height in meters
 * @param weight the weight in kilograms
 */
public record UserProfileResource(
        Long id,
        Long userId,
        String gender,
        double height,
        double weight
) {
}
