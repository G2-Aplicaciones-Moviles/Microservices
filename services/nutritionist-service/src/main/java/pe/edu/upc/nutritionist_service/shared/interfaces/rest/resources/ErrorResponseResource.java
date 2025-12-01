package pe.edu.upc.nutritionist_service.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ErrorResponseResource(
        LocalDateTime timestamp,
        String message,
        String details
) {
}