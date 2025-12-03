package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.RegisterOrUpdateProfileCommand;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.NutritionistProfileResource;

public class RegisterOrUpdateProfileCommandFromResourceAssembler {
    public static RegisterOrUpdateProfileCommand toCommand(Long userId, NutritionistProfileResource r) {
        return new RegisterOrUpdateProfileCommand(userId, r.fullName(), r.licenseNumber(), r.specialty(), r.yearsExperience());
    }
}
