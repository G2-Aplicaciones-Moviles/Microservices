package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.ExternalIngredientService;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.RecipesIntegrationClient;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.IngredientResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ExternalIngredientService that communicates with recipes-service
 * via Feign Client to validate and retrieve ingredient information.
 */
@Service
public class ExternalIngredientServiceImpl implements ExternalIngredientService {

    private final RecipesIntegrationClient recipesClient;

    public ExternalIngredientServiceImpl(RecipesIntegrationClient recipesClient) {
        this.recipesClient = recipesClient;
    }

    /**
     * Checks if an ingredient exists by name in the recipes-service.
     *
     * @param name the name of the ingredient to search for
     * @return true if the ingredient exists, false otherwise
     */
    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        try {
            List<IngredientResource> ingredients = recipesClient.getAllIngredients();
            return ingredients.stream()
                    .anyMatch(ingredient ->
                            ingredient.name().equalsIgnoreCase(name.trim()));
        } catch (Exception e) {
            System.err.println("Error checking ingredient existence by name: "
                    + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an ingredient exists by ID in the recipes-service.
     *
     * @param id the ID of the ingredient to search for
     * @return true if the ingredient exists, false otherwise
     */
    @Override
    public boolean existsById(int id) {
        try {
            recipesClient.getIngredientById(id);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking ingredient existence by ID: "
                    + e.getMessage());
            return false;
        }
    }

    /**
     * Finds an ingredient by name in the recipes-service.
     *
     * @param name the name of the ingredient to search for
     * @return an Optional containing the ingredient if found, empty otherwise
     */
    @Override
    public Optional<IngredientResource> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            List<IngredientResource> ingredients = recipesClient.getAllIngredients();
            return ingredients.stream()
                    .filter(ingredient ->
                            ingredient.name().equalsIgnoreCase(name.trim()))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding ingredient by name: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds an ingredient by ID in the recipes-service.
     *
     * @param id the ID of the ingredient to search for
     * @return an Optional containing the ingredient if found, empty otherwise
     */
    @Override
    public Optional<IngredientResource> findById(int id) {
        try {
            return Optional.ofNullable(recipesClient.getIngredientById(id));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error finding ingredient by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves all ingredients from the recipes-service.
     *
     * @return a list of all ingredients, or an empty list if an error occurs
     */
    @Override
    public List<IngredientResource> findAll() {
        try {
            return recipesClient.getAllIngredients();
        } catch (Exception e) {
            System.err.println("Error retrieving all ingredients: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
