package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.IngredientResource;

import java.util.List;

/**
 * Feign Client for communication with recipes-service.
 */
@FeignClient(name = "recipes-service", path = "/api/v1/ingredients")
public interface RecipesIntegrationClient {

    /**
     * Retrieves all ingredients from recipes-service.
     *
     * @return a list of all ingredients
     */
    @GetMapping
    List<IngredientResource> getAllIngredients();

    /**
     * Retrieves an ingredient by ID from recipes-service.
     *
     * @param id the ID of the ingredient to retrieve
     * @return the ingredient resource
     * @throws feign.FeignException.NotFound if the ingredient is not found
     */
    @GetMapping("/{id}")
    IngredientResource getIngredientById(@PathVariable("id") int id);
}
