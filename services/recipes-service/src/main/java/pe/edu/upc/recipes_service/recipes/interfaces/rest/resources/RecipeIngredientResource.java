package pe.edu.upc.recipes_service.recipes.interfaces.rest.resources;

public record RecipeIngredientResource(
        IngredientResource ingredient,
        double amountGrams
) {}
