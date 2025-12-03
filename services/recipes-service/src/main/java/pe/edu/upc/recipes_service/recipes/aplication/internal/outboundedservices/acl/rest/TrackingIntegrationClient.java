package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.MacronutrientValuesResource;

import java.util.List;

/**
 * Feign Client for communication with tracking-service.
 */
@FeignClient(name = "tracking-service", path = "/api/v1/macronutrient-values")
public interface TrackingIntegrationClient {

    @GetMapping
    List<MacronutrientValuesResource> getAllMacronutrientValues();

    @GetMapping("/{id}")
    MacronutrientValuesResource getMacronutrientValuesById(@PathVariable("id") Long id);
}
