package pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices;

/**
 * Service interface for validating user profile information from the profiles-service.
 */
public interface ExternalUserProfileService {

    /**
     * Validates that a user profile exists.
     *
     * @param profileId the user profile ID
     * @throws IllegalArgumentException if the profile does not exist
     */
    void validateProfileExists(Long profileId);

    /**
     * Checks if a user profile exists by ID.
     *
     * @param profileId the user profile ID
     * @return true if the profile exists, false otherwise
     */
    boolean existsProfile(Long profileId);
}
