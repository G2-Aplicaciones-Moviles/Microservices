package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.ExternalUserService;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.IamIntegrationClient;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.UserResource;

@Service
public class ExternalUserServiceImpl implements ExternalUserService {

    private final IamIntegrationClient iamIntegrationClient;

    public ExternalUserServiceImpl(IamIntegrationClient iamIntegrationClient) {
        this.iamIntegrationClient = iamIntegrationClient;
    }

    @Override
    public boolean userExists(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }

        try {
            UserResource user = iamIntegrationClient.getUserById(userId);
            return user != null && user.username() != null && !user.username().isEmpty();
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error comunicando con IAM Service: " + e.getMessage());
            return false;
        }
    }
}