package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.UpdateNutritionistCommand;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.UpdateNutritionistResource;

public class UpdateNutritionistCommandFromResourceAssembler {
    public static UpdateNutritionistCommand toCommandFromResource(Long nutritionistId, UpdateNutritionistResource r) {
        return new UpdateNutritionistCommand(
                nutritionistId,
                r.fullName(),
                r.bio(),
                r.profilePictureUrl(),
                r.acceptingNewPatients(),
                r.yearsExperience()
        );
    }
}
