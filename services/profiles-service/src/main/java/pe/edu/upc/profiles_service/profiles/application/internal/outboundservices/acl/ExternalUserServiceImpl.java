package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.ExternalUserService;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.IamIntegrationClient;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.UserResource;

/**
 * Implementation of ExternalUserService that communicates with iam-service
 * via Feign Client to validate user information.
 */
@Service
public class ExternalUserServiceImpl implements ExternalUserService {

    private final IamIntegrationClient iamIntegrationClient;

    public ExternalUserServiceImpl(IamIntegrationClient iamIntegrationClient) {
        this.iamIntegrationClient = iamIntegrationClient;
    }

    /**
     * Checks if a user exists by ID in the iam-service.
     *
     * @param userId the ID of the user to search for
     * @return true if the user exists, false otherwise
     */
    @Override
    public boolean userExists(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }

        try {
            UserResource user = iamIntegrationClient.getUserById(userId);
            return user != null
                    && user.username() != null
                    && !user.username().isEmpty();
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error communicating with IAM Service: "
                    + e.getMessage());
            return false;
        }
    }
}
