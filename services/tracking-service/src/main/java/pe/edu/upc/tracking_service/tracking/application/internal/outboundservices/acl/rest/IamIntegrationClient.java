package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.UserResource;

/**
 * Feign Client for communication with iam-service.
 */
@FeignClient(name = "iam-service", path = "/api/v1/users")
public interface IamIntegrationClient {

    /**
     * Retrieves a user by ID from iam-service.
     *
     * @param userId the ID of the user to retrieve
     * @return the user resource
     * @throws feign.FeignException.NotFound if the user is not found
     */
    @GetMapping("/{userId}")
    UserResource getUserById(@PathVariable("userId") Long userId);
}

