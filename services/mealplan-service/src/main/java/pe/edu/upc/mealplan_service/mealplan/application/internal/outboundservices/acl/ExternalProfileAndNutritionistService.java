package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.ProfileServiceClient;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.NutritionistServiceClient;

/**
 * Servicio externo para validar perfiles de usuario y nutricionistas.
 * Usa Feign clients para comunicarse con los servicios externos.
 * Los clientes son opcionales - si no están disponibles, solo logea advertencias.
 */
@Service
public class ExternalProfileAndNutritionistService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalProfileAndNutritionistService.class);

    private final ProfileServiceClient profileServiceClient;
    private final NutritionistServiceClient nutritionistServiceClient;

    public ExternalProfileAndNutritionistService(
            @Autowired(required = false) ProfileServiceClient profileServiceClient,
            @Autowired(required = false) NutritionistServiceClient nutritionistServiceClient) {
        this.profileServiceClient = profileServiceClient;
        this.nutritionistServiceClient = nutritionistServiceClient;

        if (profileServiceClient == null) {
            logger.warn("ProfileServiceClient not available - user validations will be skipped");
        }
        if (nutritionistServiceClient == null) {
            logger.warn("NutritionistServiceClient not available - nutritionist validations will be skipped");
        }
    }

    /**
     * Valida que un perfil de usuario exista.
     * Si el cliente no está disponible, solo logea una advertencia.
     * @param userId ID del usuario a validar
     * @throws IllegalArgumentException si el usuario no existe
     */
    public void validateUserProfile(Integer userId) {
        if (userId == null || userId <= 0) {
            logger.error("Invalid user profile ID: {}", userId);
            throw new IllegalArgumentException("Invalid user profile ID");
        }

        if (profileServiceClient == null) {
            logger.warn("ProfileServiceClient not available - skipping validation for user {}", userId);
            return;
        }

        logger.info("Validating user profile with ID: {}", userId);

        try {
            var profile = profileServiceClient.getUserProfileById(userId.longValue());
            if (profile == null) {
                logger.error("User profile not found: {}", userId);
                throw new IllegalArgumentException("User profile not found: " + userId);
            }
            logger.info("User profile {} validated successfully", userId);
        } catch (FeignException.NotFound e) {
            logger.error("User profile not found: {}", userId);
            throw new IllegalArgumentException("No regular user profile found with ID: " + userId);
        } catch (Exception e) {
            logger.error("Error validating user profile {}: {}", userId, e.getMessage());
            throw new IllegalArgumentException("Error validating user profile: " + userId);
        }
    }

    /**
     * Valida que un nutricionista exista.
     * Si el cliente no está disponible, solo logea una advertencia.
     * @param userId ID del usuario nutricionista a validar
     * @throws IllegalArgumentException si el nutricionista no existe
     */
    public void validateNutritionist(Integer userId) {
        if (userId == null || userId <= 0) {
            logger.error("Invalid nutritionist ID: {}", userId);
            throw new IllegalArgumentException("Invalid nutritionist ID");
        }

        if (nutritionistServiceClient == null) {
            logger.warn("NutritionistServiceClient not available - skipping validation for nutritionist {}", userId);
            return;
        }

        logger.info("Validating nutritionist with user ID: {}", userId);

        try {
            var nutritionist = nutritionistServiceClient.getNutritionistByUserId(userId.longValue());
            if (nutritionist == null) {
                logger.error("Nutritionist not found: {}", userId);
                throw new IllegalArgumentException("Nutritionist not found: " + userId);
            }
            logger.info("Nutritionist {} validated successfully", userId);
        } catch (FeignException.NotFound e) {
            logger.error("Nutritionist not found with userId: {}", userId);
            throw new IllegalArgumentException("No nutritionist found with userId: " + userId);
        } catch (Exception e) {
            logger.error("Error validating nutritionist {}: {}", userId, e.getMessage());
            throw new IllegalArgumentException("Error validating nutritionist: " + userId);
        }
    }
}

