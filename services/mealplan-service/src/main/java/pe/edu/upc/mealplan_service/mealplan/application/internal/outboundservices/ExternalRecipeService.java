package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices;

import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeNutritionResource;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeResource;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for retrieving recipe information from the recipes-service.
 */
public interface ExternalRecipeService {

    /**
     * Retrieves a recipe by ID.
     *
     * @param recipeId the ID of the recipe
     * @return an Optional containing the recipe if found, empty otherwise
     */
    Optional<RecipeResource> fetchRecipeById(int recipeId);

    /**
     * Retrieves all recipes.
     *
     * @return a list of all recipes
     */
    List<RecipeResource> fetchAllRecipes();

    /**
     * Retrieves nutrition information for a recipe.
     *
     * @param recipeId the ID of the recipe
     * @return the nutrition information
     * @throws IllegalArgumentException if the recipe is not found
     */
    RecipeNutritionResource fetchNutrition(int recipeId);

    /**
     * Checks if a recipe exists by ID.
     *
     * @param recipeId the ID of the recipe
     * @return true if the recipe exists, false otherwise
     */
    boolean existsById(int recipeId);
}
