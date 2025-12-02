package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.ProfilesIntegrationClient;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.UserProfileResource;
import pe.edu.upc.tracking_service.tracking.domain.model.dto.UserProfileDto;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Service to communicate with profiles-service via Feign Client.
 */
@Service
public class ExternalUserProfileService {

    private final ProfilesIntegrationClient profilesClient;

    public ExternalUserProfileService(ProfilesIntegrationClient profilesClient) {
        this.profilesClient = profilesClient;
    }

    /**
     * Checks if a user profile exists by userId.
     *
     * @param userId the user ID value object
     * @return true if the user profile exists, false otherwise
     */
    public boolean existsByUserId(UserId userId) {
        if (userId == null || userId.userId() == null) {
            return false;
        }

        try {
            List<UserProfileResource> profiles = profilesClient.getAllUserProfiles();
            return profiles.stream()
                    .anyMatch(profile -> userId.userId().equals((long) profile.id()));
        } catch (Exception e) {
            System.err.println("Error checking user profile existence by userId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a user profile by ID.
     *
     * @param profileId the profile ID
     * @return an Optional containing the user profile if found, empty otherwise
     */
    public Optional<UserProfileResource> getUserProfileById(Long profileId) {
        try {
            UserProfileResource profile = profilesClient.getUserProfileById(profileId.intValue());
            return Optional.of(profile);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching user profile by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves a user profile DTO by ID for tracking calculations.
     *
     * @param profileId the profile ID
     * @return an Optional containing the user profile DTO if found, empty otherwise
     */
    public Optional<UserProfileDto> getUserProfileDtoById(Long profileId) {
        return getUserProfileById(profileId).map(this::mapToDto);
    }

    /**
     * Checks if a profile exists by ID.
     *
     * @param profileId the profile ID
     * @return true if the profile exists, false otherwise
     */
    public boolean existsProfile(Long profileId) {
        try {
            profilesClient.getUserProfileById(profileId.intValue());
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking profile existence by ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates that a profile exists, throws exception if not found.
     *
     * @param profileId the profile ID to validate
     * @throws IllegalArgumentException if the profile does not exist
     */
    public void validateProfileExists(Long profileId) {
        if (!existsProfile(profileId)) {
            throw new IllegalArgumentException("Profile not found with ID: " + profileId);
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
