package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.UserProfileResource;

import java.util.List;

/**
 * Feign Client for communication with profiles-service.
 */
@FeignClient(name = "profiles-service", path = "/api/v1/user-profiles")
public interface ProfilesIntegrationClient {

    @GetMapping
    List<UserProfileResource> getAllUserProfiles();

    @GetMapping("/{userProfileId}")
    UserProfileResource getUserProfileById(@PathVariable("userProfileId") int userProfileId);
}
