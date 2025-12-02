package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.NutritionistResource;

public class NutritionistResourceFromEntityAssembler {
    public static NutritionistResource toResourceFromEntity(Nutritionist entity) {
        return new NutritionistResource(
                entity.getId().intValue(),
                entity.getUserId(),
                entity.getFullName(),
                entity.getLicenseNumber(),
                entity.getSpecialty(),
                entity.getYearsExperience(),
                entity.getAcceptingNewPatients(),
                entity.getBio(),
                entity.getProfilePictureUrl()
        );
    }
}
