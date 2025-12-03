package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.IamIntegrationClient;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.UserResource;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

import java.util.Optional;

/**
 * Service to communicate with iam-service via Feign Client.
 * Uses IAM as source of truth for user existence validation.
 */
@Service
public class ExternalUserProfileService {

    private final IamIntegrationClient iamClient;

    public ExternalUserProfileService(IamIntegrationClient iamClient) {
        this.iamClient = iamClient;
    }

    /**
     * Checks if a user exists by userId in IAM service.
     *
     * @param userId the user ID value object
     * @return true if the user exists, false otherwise
     */
    public boolean existsByUserId(UserId userId) {
        if (userId == null || userId.userId() == null) {
            return false;
        }

        try {
            UserResource user = iamClient.getUserById(userId.userId());
            return user != null && user.username() != null && !user.username().isEmpty();
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking user existence by userId in IAM: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a user by ID from IAM service.
     *
     * @param userId the user ID
     * @return an Optional containing the user if found, empty otherwise
     */
    public Optional<UserResource> getUserById(Long userId) {
        try {
            UserResource user = iamClient.getUserById(userId);
            return Optional.of(user);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching user by ID from IAM: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Checks if a user exists by ID in IAM service.
     *
     * @param userId the user ID
     * @return true if the user exists, false otherwise
     */
    public boolean existsUser(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }

        try {
            iamClient.getUserById(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking user existence by ID in IAM: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates that a user exists in IAM, throws exception if not found.
     *
     * @param userId the user ID to validate
     * @throws IllegalArgumentException if the user does not exist
     */
    public void validateUserExists(Long userId) {
        if (!existsUser(userId)) {
            throw new IllegalArgumentException("User not found in IAM with ID: " + userId);
        }
    }

    /**
     * Validates that a user exists using UserId value object.
     *
     * @param userId the user ID value object
     * @throws IllegalArgumentException if the user does not exist
     */
    public void validateUserExists(UserId userId) {
        if (userId == null || userId.userId() == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        validateUserExists(userId.userId());
    }
}
