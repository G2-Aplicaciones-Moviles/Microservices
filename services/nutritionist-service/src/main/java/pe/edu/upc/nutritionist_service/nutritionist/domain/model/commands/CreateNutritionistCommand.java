package pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.Specialty;

public record CreateNutritionistCommand(
        Long userId,
        String fullName,
        String licenseNumber,
        Specialty specialty,
        Integer yearsExperience,
        Boolean acceptingNewPatients,
        String bio,
        String profilePictureUrl
) {}
