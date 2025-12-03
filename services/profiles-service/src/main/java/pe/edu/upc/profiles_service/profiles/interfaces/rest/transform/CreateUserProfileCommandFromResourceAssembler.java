package pe.edu.upc.profiles_service.profiles.interfaces.rest.transform;

import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateUserProfileResource;

public class CreateUserProfileCommandFromResourceAssembler {

    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
                resource.userId(),
                resource.gender(),
                resource.height(),
                resource.weight(),
                resource.userScore(),
                resource.activityLevelId(),
                resource.objectiveId(),
                resource.allergyIds(),
                resource.birthDate() // ✅ agregado
        );

    }
}
