package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

public record IngredientResource(int id, String name, double calories, double proteins, double fats,
                                 double carbohydrates, Long macronutrientValuesId) {
}
