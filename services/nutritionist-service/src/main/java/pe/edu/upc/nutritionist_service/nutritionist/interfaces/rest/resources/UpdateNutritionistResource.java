package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources;

public record UpdateNutritionistResource(
        String fullName,
        String bio,
        String profilePictureUrl,
        Boolean acceptingNewPatients,
        Integer yearsExperience
) {}
