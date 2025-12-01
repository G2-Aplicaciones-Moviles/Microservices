package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.Specialty;

public record NutritionistResource(
        Integer id,
        Long userId,
        String fullName,
        String licenseNumber,
        Specialty specialty,
        Integer yearsExperience,
        Boolean acceptingNewPatients,
        String bio,
        String profilePictureUrl
) {}
