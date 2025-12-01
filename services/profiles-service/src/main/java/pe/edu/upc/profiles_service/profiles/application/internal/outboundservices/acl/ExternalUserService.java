package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.iam.interfaces.acl.IamContextFacade;

@Service
public class ExternalUserService {

    private final IamContextFacade iamContextFacade;

    public ExternalUserService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean userExists(Long userId) {
        if (userId == null || userId <= 0) return false;
        String username = iamContextFacade.fetchUsernameByUserId(userId);
        return !username.isEmpty();
    }
}
