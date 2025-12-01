package pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.ExternalUserProfileService;
import pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl.rest.ProfilesIntegrationClient;

/**
 * Implementation of ExternalUserProfileService that communicates with profiles-service
 * via Feign Client to validate user profile information.
 */
@Service
public class ExternalUserProfileServiceImpl implements ExternalUserProfileService {

    private final ProfilesIntegrationClient profilesClient;

    public ExternalUserProfileServiceImpl(ProfilesIntegrationClient profilesClient) {
        this.profilesClient = profilesClient;
    }

    /**
     * Validates that a user profile exists by ID in the profiles-service.
     *
     * @param profileId the user profile ID
     * @throws IllegalArgumentException if the profile does not exist
     */
    @Override
    public void validateProfileExists(Long profileId) {
        if (!existsProfile(profileId)) {
            throw new IllegalArgumentException("Profile not found with ID: " + profileId);
        }
    }

    /**
     * Checks if a user profile exists by ID in the profiles-service.
     *
     * @param profileId the user profile ID
     * @return true if the profile exists, false otherwise
     */
    @Override
    public boolean existsProfile(Long profileId) {
        if (profileId == null || profileId <= 0) {
            return false;
        }

        try {
            profilesClient.getUserProfileById(profileId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking profile existence: " + e.getMessage());
            return false;
        }
    }
}
