package pe.edu.upc.goals_service.goals.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;

@Service("goalsExternalProfilesService")
public class ExternalProfilesService {
    public boolean existsProfile(Long profileId) {
        // TODO: integrar con ProfilesContextFacade si lo tienes
        return true;
    }
}