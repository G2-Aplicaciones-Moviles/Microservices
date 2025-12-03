package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.ProfilesIntegrationClient;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.UserProfileResource;
import pe.edu.upc.tracking_service.tracking.domain.model.dto.UserProfileDto;

import java.util.Optional;

/**
 * Service to communicate with profiles-service via Feign Client.
 * Provides access to detailed profile information (health data, activity level, objectives).
 */
@Service
public class ExternalProfileService {

    private final ProfilesIntegrationClient profilesClient;

    public ExternalProfileService(ProfilesIntegrationClient profilesClient) {
        this.profilesClient = profilesClient;
    }

    /**
     * Retrieves a user profile by user ID.
     *
     * @param userId the user ID
     * @return an Optional containing the user profile if found, empty otherwise
     */
    public Optional<UserProfileResource> getUserProfileByUserId(Long userId) {
        try {
            UserProfileResource profile = profilesClient.getUserProfileByUserId(userId);
            return Optional.of(profile);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching user profile by userId from Profiles: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves a user profile DTO by user ID for tracking calculations.
     *
     * @param userId the user ID
     * @return an Optional containing the user profile DTO if found, empty otherwise
     */
    public Optional<UserProfileDto> getUserProfileDtoByUserId(Long userId) {
        return getUserProfileByUserId(userId).map(this::mapToDto);
    }

    /**
     * Checks if a profile exists for a given user ID.
     *
     * @param userId the user ID
     * @return true if the profile exists, false otherwise
     */
    public boolean existsProfileByUserId(Long userId) {
        try {
            profilesClient.getUserProfileByUserId(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking profile existence by userId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates that a profile exists for the given user ID.
     *
     * @param userId the user ID to validate
     * @throws IllegalArgumentException if the profile does not exist
     */
    public void validateProfileExists(Long userId) {
        if (!existsProfileByUserId(userId)) {
            throw new IllegalArgumentException("Profile not found for user ID: " + userId);
        }
    }

    /**
     * Maps UserProfileResource to UserProfileDto for tracking calculations.
     *
     * @param resource the user profile resource
     * @return the user profile DTO
     */
    private UserProfileDto mapToDto(UserProfileResource resource) {
        double activityFactor = calculateActivityFactor(resource.activityLevelName());

        return new UserProfileDto(
                (long) resource.id(),
                resource.gender(),
                resource.height() != null ? resource.height() : 0.0,
                resource.weight() != null ? resource.weight() : 0.0,
                activityFactor,
                resource.objectiveName(),
                resource.birthDate()
        );
    }

    /**
     * Calculates activity factor from activity level string.
     *
     * @param activityLevel the activity level string
     * @return the activity factor
     */
    private double calculateActivityFactor(String activityLevel) {
        if (activityLevel == null) {
            return 1.2;
        }

        return switch (activityLevel.toUpperCase()) {
            case "LIGHT", "LIGERO" -> 1.375;
            case "MODERATE", "MODERADO" -> 1.55;
            case "ACTIVE", "ACTIVO" -> 1.725;
            case "VERY_ACTIVE", "MUY_ACTIVO" -> 1.9;
            default -> 1.2;
        };
    }
}

