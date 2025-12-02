package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.NutritionistResource;

/**
 * Feign client for communicating with the nutritionist-service.
 * Uses url placeholder - configure in application.properties with:
 * nutritionist-service.url=http://localhost:8082
 */
@FeignClient(name = "nutritionist-service", url = "${nutritionist-service.url:http://localhost:8082}", path = "/api/v1/nutritionists")
public interface NutritionistServiceClient {

    /**
     * Retrieves a nutritionist by user ID.
     *
     * @param userId the user ID of the nutritionist
     * @return the nutritionist resource
     */
    @GetMapping("/user/{userId}")
    NutritionistResource getNutritionistByUserId(@PathVariable("userId") Long userId);
}

