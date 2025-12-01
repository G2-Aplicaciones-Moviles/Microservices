package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.RecipesIntegrationClient;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.IngredientResource;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para validar ingredientes desde recipes-service
 */
@Service
public class ExternalIngredientService {

    private final RecipesIntegrationClient recipesClient;

    public ExternalIngredientService(RecipesIntegrationClient recipesClient) {
        this.recipesClient = recipesClient;
    }

    /**
     * Verifica si existe un ingrediente por nombre
     */
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        try {
            List<IngredientResource> ingredients = recipesClient.getAllIngredients();
            return ingredients.stream()
                    .anyMatch(ingredient -> ingredient.name().equalsIgnoreCase(name.trim()));
        } catch (Exception e) {
            // Log error y retornar false si el servicio no está disponible
            System.err.println("Error al consultar recipes-service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si existe un ingrediente por ID
     */
    public boolean existsById(int id) {
        try {
            Optional<IngredientResource> ingredient = recipesClient.getIngredientById(id);
            return ingredient.isPresent();
        } catch (Exception e) {
            System.err.println("Error al consultar recipes-service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un ingrediente por nombre
     */
    public Optional<IngredientResource> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            List<IngredientResource> ingredients = recipesClient.getAllIngredients();
            return ingredients.stream()
                    .filter(ingredient -> ingredient.name().equalsIgnoreCase(name.trim()))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error al consultar recipes-service: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtiene un ingrediente por ID
     */
    public Optional<IngredientResource> findById(int id) {
        try {
            return recipesClient.getIngredientById(id);
        } catch (Exception e) {
            System.err.println("Error al consultar recipes-service: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtiene todos los ingredientes
     */
    public List<IngredientResource> findAll() {
        try {
            return recipesClient.getAllIngredients();
        } catch (Exception e) {
            System.err.println("Error al consultar recipes-service: " + e.getMessage());
            return List.of();
        }
    }
}

