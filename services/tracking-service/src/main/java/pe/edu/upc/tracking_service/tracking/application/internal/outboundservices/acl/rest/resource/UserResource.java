package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource;

import java.util.List;

/**
 * DTO for user information from iam-service.
 */
public record UserResource(Long id, String username, List<String> roles) {
}

