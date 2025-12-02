package pe.edu.upc.profiles_service.profiles.interfaces.rest.transform;

import pe.edu.upc.profiles_service.profiles.domain.model.commands.UpdateUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateUserProfileResource;
public class UpdateUserProfileCommandFromResourceAssembler {

    /**
     * Convierte un recurso REST y un ID a UpdateUserProfileCommand.
     */
    // En UpdateUserProfileCommandFromResourceAssembler.java
    public static UpdateUserProfileCommand toCommandFromResource(Long profileId, CreateUserProfileResource resource) {
        return new UpdateUserProfileCommand(
                profileId,
                resource.gender(),
                resource.height(),
                resource.weight(),
                resource.userScore(),
                resource.activityLevelId(),
                resource.objectiveId(),
                resource.birthDate() // ✅ agregado
        );

    }
}
