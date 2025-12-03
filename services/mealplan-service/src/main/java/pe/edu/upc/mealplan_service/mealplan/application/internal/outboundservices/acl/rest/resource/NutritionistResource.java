package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource;

/**
 * Resource representing a nutritionist from nutritionist-service.
 */
public record NutritionistResource(
        Long id,
        Long userId,
        String specialization,
        String licenseNumber
) {
}

