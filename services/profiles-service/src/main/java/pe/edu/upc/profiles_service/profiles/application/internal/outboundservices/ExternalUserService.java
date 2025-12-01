package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices;

/**
 * Service interface for validating user information from the iam-service.
 */
public interface ExternalUserService {

    /**
     * Checks if a user exists by ID.
     *
     * @param userId the ID of the user
     * @return true if the user exists, false otherwise
     */
    boolean userExists(Long userId);
}
