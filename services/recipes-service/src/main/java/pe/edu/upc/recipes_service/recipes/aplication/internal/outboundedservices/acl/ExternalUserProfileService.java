package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.ProfilesIntegrationClient;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.UserProfileResource;

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

    public boolean existsProfileById(Long profileId) {
        try {
            profilesClient.getUserProfileById(profileId.intValue());
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking profile existence: " + e.getMessage());
            return false;
        }
    }

    public Optional<UserProfileResource> getUserProfileById(Long profileId) {
        try {
            UserProfileResource profile = profilesClient.getUserProfileById(profileId.intValue());
            return Optional.of(profile);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching user profile: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getObjectiveNameByProfileId(Long profileId) {
        return getUserProfileById(profileId)
                .map(UserProfileResource::objectiveName);
    }

    public void validateUserExists(Long userId) {
        if (!existsProfileById(userId)) {
            throw new IllegalArgumentException("User profile not found with ID: " + userId);
        }
    }

    public String getValidatedObjectiveName(Long userId) {
        validateUserExists(userId);
        return getObjectiveNameByProfileId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User profile exists but has no objective defined for ID: " + userId));
    }

    public List<UserProfileResource> getAllUserProfiles() {
        try {
            return profilesClient.getAllUserProfiles();
        } catch (Exception e) {
            System.err.println("Error fetching all user profiles: " + e.getMessage());
            return List.of();
        }
    }
}

