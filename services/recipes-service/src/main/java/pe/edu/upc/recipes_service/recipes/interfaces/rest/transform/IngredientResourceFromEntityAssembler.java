package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Ingredient;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.IngredientResource;

import java.util.List;
import java.util.stream.Collectors;

public class IngredientResourceFromEntityAssembler {

    public static IngredientResource toResourceFromEntity(Ingredient ingredient) {
        return new IngredientResource(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCalories(),
                ingredient.getProteins(),
                ingredient.getFats(),
                ingredient.getCarbohydrates(),
                ingredient.getMacronutrientValuesId()
        );
    }

    public static List<IngredientResource> toResources(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(IngredientResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
