package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.NutritionistResource;

/**
 * Feign Client for communication with nutritionist-service.
 */
@FeignClient(name = "nutritionist-service", path = "/api/v1/nutritionists")
public interface NutritionistIntegrationClient {

    @GetMapping("/by-user")
    NutritionistResource getNutritionistByUserId(@RequestParam("userId") Long userId);
}

