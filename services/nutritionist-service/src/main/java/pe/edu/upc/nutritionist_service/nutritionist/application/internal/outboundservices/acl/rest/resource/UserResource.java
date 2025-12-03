package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.resource;

import java.util.List;

/**
 * DTO for user information from iam-service.
 *
 * @param id       the user ID
 * @param username the username
 * @param roles    the user roles
 */
public record UserResource(
        Long id,
        String username,
        List<String> roles
) {
}

