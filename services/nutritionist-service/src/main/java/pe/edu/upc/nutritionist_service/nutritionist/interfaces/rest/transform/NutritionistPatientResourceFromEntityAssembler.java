package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.NutritionistPatient;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.NutritionistPatientResource;

public class NutritionistPatientResourceFromEntityAssembler {

    public static NutritionistPatientResource toResourceFromEntity(NutritionistPatient np) {
        return new NutritionistPatientResource(
                np.getId().intValue(),
                np.getNutritionistId(),
                np.getPatientUserId(),
                np.getServiceType(),
                np.getStartDate(),
                np.getScheduledAt(),
                np.getAccepted(),
                np.getRequestedAt()
        );
    }
}