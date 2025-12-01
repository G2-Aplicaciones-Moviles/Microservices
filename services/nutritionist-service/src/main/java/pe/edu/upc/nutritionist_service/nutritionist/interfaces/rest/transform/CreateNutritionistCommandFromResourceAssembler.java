package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.CreateNutritionistCommand;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.CreateNutritionistResource;

public class CreateNutritionistCommandFromResourceAssembler {
    public static CreateNutritionistCommand toCommandFromResource(CreateNutritionistResource resource) {
        return new CreateNutritionistCommand(
                resource.userId(),
                resource.fullName(),
                resource.licenseNumber(),
                resource.specialty(),
                resource.yearsExperience(),
                resource.acceptingNewPatients(),
                resource.bio(),
                resource.profilePictureUrl()
        );
    }
}
