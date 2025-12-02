package pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands;

public record UpdateNutritionistCommand(
        Long nutritionistId,
        String fullName,
        String bio,
        String profilePictureUrl,
        Boolean acceptingNewPatients,
        Integer yearsExperience
) {}
