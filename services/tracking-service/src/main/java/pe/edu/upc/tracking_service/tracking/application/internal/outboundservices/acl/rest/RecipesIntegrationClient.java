package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.RecipeNutritionResource;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.RecipeResource;

import java.util.List;

/**
 * Feign Client for communication with recipes-service.
 */
@FeignClient(name = "recipes-service", path = "/api/v1/recipes")
public interface RecipesIntegrationClient {

    /**
     * Retrieves all recipes from recipes-service.
     *
     * @return a list of all recipes
     */
    @GetMapping
    List<RecipeResource> getAllRecipes();

    /**
     * Retrieves a recipe by ID from recipes-service.
     *
     * @param id the ID of the recipe to retrieve
     * @return the recipe resource
     * @throws feign.FeignException.NotFound if the recipe is not found
     */
    @GetMapping("/{recipeId}")
    RecipeResource getRecipeById(@PathVariable("recipeId") int id);

    /**
     * Retrieves nutrition information for a recipe.
     *
     * @param recipeId the ID of the recipe
     * @return the nutrition resource
     * @throws feign.FeignException.NotFound if the recipe is not found
     */
    @GetMapping("/{recipeId}/nutrition")
    RecipeNutritionResource getRecipeNutrition(@PathVariable("recipeId") int recipeId);
}

