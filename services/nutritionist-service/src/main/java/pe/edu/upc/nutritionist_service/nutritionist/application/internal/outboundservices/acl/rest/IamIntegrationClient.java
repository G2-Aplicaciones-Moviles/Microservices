package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.resource.SignUpResource;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.resource.UserResource;

/**
 * Feign Client for communication with iam-service.
 */
@FeignClient(name = "iam-service", path = "/api/v1")
public interface IamIntegrationClient {

    /**
     * Retrieves a user by ID from iam-service.
     *
     * @param userId the user ID
     * @return the user resource
     * @throws feign.FeignException.NotFound if the user is not found
     */
    @GetMapping("/users/{userId}")
    UserResource getUserById(@PathVariable("userId") Long userId);

    /**
     * Creates a new user via sign-up in iam-service.
     *
     * @param signUpResource the sign-up information
     * @return the created user resource
     */
    @PostMapping("/authentication/sign-up")
    UserResource signUp(@RequestBody SignUpResource signUpResource);
}

