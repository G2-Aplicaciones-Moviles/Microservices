package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource;

public record MacronutrientValuesResource(
        Long id,
        double calories,
        double carbs,
        double proteins,
        double fats
) {
}
