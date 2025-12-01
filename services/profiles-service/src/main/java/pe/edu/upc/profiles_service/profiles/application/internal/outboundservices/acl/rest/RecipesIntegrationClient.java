package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.IngredientResource;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "recipes-service", path = "/api/v1/ingredients")
public interface RecipesIntegrationClient {

    @GetMapping
    List<IngredientResource> getAllIngredients();

    @GetMapping("/{id}")
    Optional<IngredientResource> getIngredientById(@PathVariable("id") int id);
}
