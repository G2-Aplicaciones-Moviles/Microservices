package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.UpdateAvailabilityCommand;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.AvailabilityConfigResource;

public class UpdateAvailabilityCommandFromResourceAssembler {
    public static UpdateAvailabilityCommand toCommand(Long userId, AvailabilityConfigResource r) {
        return new UpdateAvailabilityCommand(userId, r.acceptingNewPatients());
    }
}
