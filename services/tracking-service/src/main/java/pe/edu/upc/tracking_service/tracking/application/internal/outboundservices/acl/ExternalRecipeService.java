package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.RecipesIntegrationClient;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.RecipeNutritionResource;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.RecipeId;

import java.util.Optional;

/**
 * Service to communicate with recipes-service via Feign Client.
 */
@Service
public class ExternalRecipeService {

    private final RecipesIntegrationClient recipesClient;

    public ExternalRecipeService(RecipesIntegrationClient recipesClient) {
        this.recipesClient = recipesClient;
    }

    /**
     * Checks if a recipe exists by ID.
     *
     * @param recipeId the recipe ID value object
     * @return true if the recipe exists, false otherwise
     */
    public boolean existsByRecipeId(RecipeId recipeId) {
        try {
            recipesClient.getRecipeById(Math.toIntExact(recipeId.recipeId()));
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking recipe existence by ID: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves nutrition information for a recipe.
     *
     * @param recipeId the recipe ID value object
     * @return an Optional containing the nutrition resource if found, empty otherwise
     */
    public Optional<RecipeNutritionResource> fetchNutritionByRecipeId(RecipeId recipeId) {
        try {
            RecipeNutritionResource nutrition = recipesClient.getRecipeNutrition(Math.toIntExact(recipeId.recipeId()));
            return Optional.of(nutrition);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching nutrition for recipe ID: " + e.getMessage());
            return Optional.empty();
        }
    }
}