package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.ExternalUserService;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.IamIntegrationClient;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.resource.SignUpResource;

import java.util.List;

/**
 * Implementation of ExternalUserService that communicates with iam-service
 * via Feign Client to validate and create users.
 */
@Service
public class ExternalUserServiceImpl implements ExternalUserService {

    private final IamIntegrationClient iamClient;

    public ExternalUserServiceImpl(IamIntegrationClient iamClient) {
        this.iamClient = iamClient;
    }

    /**
     * Creates a nutritionist user in the iam-service.
     *
     * @param username the username
     * @param password the password
     * @return the created user ID
     */
    @Override
    public Long createNutritionistUser(String username, String password) {
        try {
            var signUpResource = new SignUpResource(
                    username,
                    password,
                    List.of("ROLE_NUTRITIONIST")
            );
            var createdUser = iamClient.signUp(signUpResource);
            return createdUser.id();
        } catch (Exception e) {
            System.err.println("Error creating nutritionist user: " + e.getMessage());
            throw new IllegalArgumentException("Failed to create user in IAM service");
        }
    }

    /**
     * Checks if a user exists by ID in the iam-service.
     *
     * @param userId the user ID
     * @return true if the user exists, false otherwise
     */
    @Override
    public boolean userExists(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }

        try {
            var user = iamClient.getUserById(userId);
            return user != null && user.username() != null && !user.username().isBlank();
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates that a user does NOT exist before creating a nutritionist.
     * This prevents duplicate nutritionist registrations for existing users.
     *
     * @param userId the user ID
     * @throws IllegalArgumentException if the user already exists
     */
    @Override
    public void validateUserNotExists(Long userId) {
        if (userExists(userId)) {
            throw new IllegalArgumentException(
                    "User with ID " + userId + " is already registered in the system");
        }
    }
}

