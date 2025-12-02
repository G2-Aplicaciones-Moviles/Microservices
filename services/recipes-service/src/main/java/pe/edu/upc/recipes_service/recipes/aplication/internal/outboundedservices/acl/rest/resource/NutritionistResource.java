package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource;

public record NutritionistResource(
        Integer id,
        Long userId,
        String fullName,
        String licenseNumber,
        String specialty,
        Integer yearsExperience,
        Boolean acceptingNewPatients,
        String bio,
        String profilePictureUrl
) {
}

