package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource;

public record IngredientResource(
        int id,
        String name,
        double calories,
        double proteins,
        double fats,
        double carbohydrates,
        Long macronutrientValuesId
) {
}
