package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeIngredient;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.IngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeIngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeResourceFromEntityAssembler {

    public static RecipeResource toResourceFromEntity(Recipe recipe) {
        var ingredientResources = recipe.getRecipeIngredients()
                .stream()
                .map(ri -> new RecipeIngredientResource(
                        new IngredientResource(
                                ri.getIngredient().getId().intValue(),
                                ri.getIngredient().getName(),
                                ri.getIngredient().getCalories(),
                                ri.getIngredient().getProteins(),
                                ri.getIngredient().getFats(),
                                ri.getIngredient().getCarbohydrates(),
                                ri.getIngredient().getMacronutrientValuesId()
                        ),
                        ri.getAmountGrams()
                ))
                .toList();

        return new RecipeResource(
                recipe.getId().intValue(),
                recipe.getCreatedByNutritionistId(),
                recipe.getAssignedToProfileId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getPreparationTime(),
                recipe.getDifficulty(),
                recipe.getCategory().getName(),
                recipe.getRecipeType().getName(),
                ingredientResources
        );
    }

    public static List<RecipeResource> toResources(List<Recipe> recipes) {
        return recipes.stream()
                .map(RecipeResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
    public static List<RecipeIngredientResource> mapRecipeIngredientsToResources(Set<RecipeIngredient> ingredients) {
        return ingredients.stream()
                .map(ri -> new RecipeIngredientResource(
                        new IngredientResource(
                                ri.getIngredient().getId().intValue(),
                                ri.getIngredient().getName(),
                                ri.getIngredient().getCalories(),
                                ri.getIngredient().getProteins(),
                                ri.getIngredient().getFats(),
                                ri.getIngredient().getCarbohydrates(),
                                ri.getIngredient().getMacronutrientValuesId()
                        ),
                        ri.getAmountGrams()
                ))
                .toList();
    }
}