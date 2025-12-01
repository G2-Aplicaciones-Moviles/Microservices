package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource;

/**
 * DTO for ingredient information from recipes-service.
 *
 * @param id                    the ingredient ID
 * @param name                  the ingredient name
 * @param calories              the calories per serving
 * @param proteins              the protein content in grams
 * @param fats                  the fat content in grams
 * @param carbohydrates         the carbohydrate content in grams
 * @param macronutrientValuesId the ID of associated macronutrient values
 */
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
