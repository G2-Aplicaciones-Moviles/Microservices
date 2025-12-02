package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.CreateNutritionistPatientCommand;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.CreateNutritionistPatientResource;

public class CreateNutritionistPatientCommandFromResourceAssembler {

    public static CreateNutritionistPatientCommand toCommandFromResource(CreateNutritionistPatientResource r) {
        return new CreateNutritionistPatientCommand(
                r.nutritionistId(),
                r.patientUserId(),
                r.serviceType(),
                r.startDate(),
                r.scheduledAt()
        );
    }
}
