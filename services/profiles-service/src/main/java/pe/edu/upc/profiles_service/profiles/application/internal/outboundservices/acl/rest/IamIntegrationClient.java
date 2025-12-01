package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource.UserResource;

@FeignClient(name = "iam-service")
public interface IamIntegrationClient {
    @GetMapping("/api/v1/users/{userId}")
    UserResource getUserById(@PathVariable("userId") Long userId);
}
