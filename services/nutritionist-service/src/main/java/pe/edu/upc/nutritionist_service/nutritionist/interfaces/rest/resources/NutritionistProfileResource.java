package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.Specialty;

public record NutritionistProfileResource(
        String fullName,
        String licenseNumber,
        Specialty specialty,
        Integer yearsExperience
) {}
