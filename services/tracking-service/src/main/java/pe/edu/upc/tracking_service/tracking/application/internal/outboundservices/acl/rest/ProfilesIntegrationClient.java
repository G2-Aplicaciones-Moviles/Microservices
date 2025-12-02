package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.UserProfileResource;

import java.util.List;

/**
 * Feign Client for communication with profiles-service.
 */
@FeignClient(name = "profiles-service", path = "/api/v1/user-profiles")
public interface ProfilesIntegrationClient {

    /**
     * Retrieves all user profiles from profiles-service.
     *
     * @return a list of all user profiles
     */
    @GetMapping
    List<UserProfileResource> getAllUserProfiles();

    /**
     * Retrieves a user profile by ID from profiles-service.
     *
     * @param userProfileId the ID of the user profile to retrieve
     * @return the user profile resource
     * @throws feign.FeignException.NotFound if the user profile is not found
     */
    @GetMapping("/{userProfileId}")
    UserProfileResource getUserProfileById(@PathVariable("userProfileId") int userProfileId);
}

