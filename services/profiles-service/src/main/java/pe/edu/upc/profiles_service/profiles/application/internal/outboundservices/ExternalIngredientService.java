package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices;

import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.IngredientResource;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for validating and retrieving ingredient information
 * from the recipes-service.
 */
public interface ExternalIngredientService {

    /**
     * Checks if an ingredient exists by name.
     *
     * @param name the name of the ingredient
     * @return true if the ingredient exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Checks if an ingredient exists by ID.
     *
     * @param id the ID of the ingredient
     * @return true if the ingredient exists, false otherwise
     */
    boolean existsById(int id);

    /**
     * Finds an ingredient by name.
     *
     * @param name the name of the ingredient
     * @return an Optional containing the ingredient if found, empty otherwise
     */
    Optional<IngredientResource> findByName(String name);

    /**
     * Finds an ingredient by ID.
     *
     * @param id the ID of the ingredient
     * @return an Optional containing the ingredient if found, empty otherwise
     */
    Optional<IngredientResource> findById(int id);

    /**
     * Retrieves all ingredients.
     *
     * @return a list of all ingredients
     */
    List<IngredientResource> findAll();
}
