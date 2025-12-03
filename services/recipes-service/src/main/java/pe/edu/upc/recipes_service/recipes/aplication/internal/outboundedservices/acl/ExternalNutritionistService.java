package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.NutritionistIntegrationClient;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.NutritionistResource;

import java.util.Optional;

/**
 * Service to communicate with nutritionist-service via Feign Client.
 */
@Service
public class ExternalNutritionistService {

    private final NutritionistIntegrationClient nutritionistClient;

    public ExternalNutritionistService(NutritionistIntegrationClient nutritionistClient) {
        this.nutritionistClient = nutritionistClient;
    }

    public Optional<NutritionistResource> getNutritionistByUserId(Long userId) {
        try {
            NutritionistResource nutritionist = nutritionistClient.getNutritionistByUserId(userId);
            return Optional.of(nutritionist);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching nutritionist by userId " + userId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public String getNutritionistNameOrDefault(Long userId) {
        return getNutritionistByUserId(userId)
                .map(NutritionistResource::fullName)
                .orElse("Unknown Nutritionist");
    }
}

