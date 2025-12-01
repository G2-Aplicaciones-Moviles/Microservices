package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.iam.interfaces.acl.IamContextFacade;

import java.util.List;

@Service
public class IamExternalService {
    private final IamContextFacade iamContextFacade;

    public IamExternalService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Long createNutritionistUser(String username, String password) {
        return iamContextFacade.createUser(username, password, List.of("ROLE_NUTRITIONIST"));
    }

    public boolean userExists(Long userId) {
        String username = iamContextFacade.fetchUsernameByUserId(userId);
        return username != null && !username.isBlank();
    }
}
