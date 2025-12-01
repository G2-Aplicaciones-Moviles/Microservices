package pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl.rest.resource.UserProfileResource;

/**
 * Feign Client for communication with profiles-service.
 */
@FeignClient(name = "profiles-service", path = "/api/v1/user-profiles")
public interface ProfilesIntegrationClient {

    /**
     * Retrieves a user profile by ID from profiles-service.
     *
     * @param id the user profile ID
     * @return the user profile resource
     * @throws feign.FeignException.NotFound if the profile is not found
     */
    @GetMapping("/{id}")
    UserProfileResource getUserProfileById(@PathVariable("id") Long id);
}
