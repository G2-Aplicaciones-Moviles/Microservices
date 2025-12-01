package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.ExternalRecipeService;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.RecipesIntegrationClient;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeNutritionResource;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ExternalRecipeService that communicates with recipes-service
 * via Feign Client to retrieve recipe information.
 */
@Service
public class ExternalRecipeServiceImpl implements ExternalRecipeService {

    private final RecipesIntegrationClient recipesClient;

    public ExternalRecipeServiceImpl(RecipesIntegrationClient recipesClient) {
        this.recipesClient = recipesClient;
    }

    /**
     * Retrieves a recipe by ID from the recipes-service.
     *
     * @param recipeId the ID of the recipe to retrieve
     * @return an Optional containing the recipe if found, empty otherwise
     */
    @Override
    public Optional<RecipeResource> fetchRecipeById(int recipeId) {
        try {
            RecipeResource recipe = recipesClient.getRecipeById(recipeId);
            return Optional.ofNullable(recipe);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching recipe by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves all recipes from the recipes-service.
     *
     * @return a list of all recipes, or an empty list if an error occurs
     */
    @Override
    public List<RecipeResource> fetchAllRecipes() {
        try {
            return recipesClient.getAllRecipes();
        } catch (Exception e) {
            System.err.println("Error fetching all recipes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves nutrition information for a recipe from the recipes-service.
     *
     * @param recipeId the ID of the recipe
     * @return the nutrition information
     * @throws IllegalArgumentException if the recipe is not found
     */
    @Override
    public RecipeNutritionResource fetchNutrition(int recipeId) {
        try {
            return recipesClient.getRecipeNutrition(recipeId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Recipe not found with ID: " + recipeId);
        } catch (Exception e) {
            System.err.println("Error fetching recipe nutrition: " + e.getMessage());
            throw new IllegalArgumentException("Error fetching nutrition for recipe: "
                    + recipeId);
        }
    }

    /**
     * Checks if a recipe exists by ID in the recipes-service.
     *
     * @param recipeId the ID of the recipe
     * @return true if the recipe exists, false otherwise
     */
    @Override
    public boolean existsById(int recipeId) {
        try {
            recipesClient.getRecipeById(recipeId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking recipe existence: " + e.getMessage());
            return false;
        }
    }
}
