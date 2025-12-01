package pe.edu.upc.iam_service.iam.interfaces.rest.resources;

import java.util.Set;

public record AuthenticatedUserResource(Long id, String email, String token, Set<String> roles) {
}
