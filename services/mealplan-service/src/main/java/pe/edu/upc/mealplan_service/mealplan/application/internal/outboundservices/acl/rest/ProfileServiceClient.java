package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.UserProfileResource;

/**
 * Feign client for communicating with the profiles-service.
 * Uses url placeholder - configure in application.properties with:
 * profiles-service.url=http://localhost:8081
 */
@FeignClient(name = "profiles-service", url = "${profiles-service.url:http://localhost:8081}", path = "/api/v1/profiles")
public interface ProfileServiceClient {

    /**
     * Retrieves a user profile by ID.
     *
     * @param userId the user ID
     * @return the user profile resource
     */
    @GetMapping("/{userId}")
    UserProfileResource getUserProfileById(@PathVariable("userId") Long userId);
}

