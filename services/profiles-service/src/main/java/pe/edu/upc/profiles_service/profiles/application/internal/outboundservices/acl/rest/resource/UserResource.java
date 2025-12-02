package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.resource;

/**
 * DTO for user information from iam-service.
 *
 * @param id       the user ID
 * @param username the username
 */
public record UserResource(Long id, String username) {
}
