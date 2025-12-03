package pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest.resource;

import java.util.List;

/**
 * DTO for user sign-up request to iam-service.
 *
 * @param username the username
 * @param password the password
 * @param roles    the user roles
 */
public record SignUpResource(
        String username,
        String password,
        List<String> roles
) {
}

