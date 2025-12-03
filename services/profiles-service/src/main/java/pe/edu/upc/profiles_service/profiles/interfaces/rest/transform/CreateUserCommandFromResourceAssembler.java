package pe.edu.upc.profiles_service.profiles.interfaces.rest.transform;

import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateProfileCommand;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateProfileResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource r) {
        return new CreateProfileCommand(
                r.name(),
                r.email(),
                r.password(),
                r.isActive(),
                r.birthDate(),
                r.userProfileId()
        );
    }
}
