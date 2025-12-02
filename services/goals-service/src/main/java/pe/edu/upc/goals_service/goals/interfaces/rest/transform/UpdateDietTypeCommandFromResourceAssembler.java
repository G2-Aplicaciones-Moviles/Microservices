package pe.edu.upc.goals_service.goals.interfaces.rest.transform;

import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateDietTypeCommand;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.DietTypeConfigResource;

public class UpdateDietTypeCommandFromResourceAssembler {
    public static UpdateDietTypeCommand toCommand(Long userId, DietTypeConfigResource r) {
        return new UpdateDietTypeCommand(userId, r.preset());
    }
}