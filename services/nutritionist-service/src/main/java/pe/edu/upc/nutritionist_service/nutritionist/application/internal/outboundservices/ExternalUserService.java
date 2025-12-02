package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices;

/**
 * Service interface for validating user information from the iam-service.
 */
public interface ExternalUserService {

    /**
     * Creates a nutritionist user in the iam-service.
     *
     * @param username the username
     * @param password the password
     * @return the created user ID
     */
    Long createNutritionistUser(String username, String password);

    /**
     * Checks if a user exists by ID.
     *
     * @param userId the user ID
     * @return true if the user exists, false otherwise
     */
    boolean userExists(Long userId);

    /**
     * Validates that a user does NOT exist before creating a nutritionist.
     *
     * @param userId the user ID
     * @throws IllegalArgumentException if the user already exists
     */
    void validateUserNotExists(Long userId);
}

