package pe.edu.upc.goals_service.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ErrorResponseResource(
        LocalDateTime timestamp,
        String message,
        String details
) {
}