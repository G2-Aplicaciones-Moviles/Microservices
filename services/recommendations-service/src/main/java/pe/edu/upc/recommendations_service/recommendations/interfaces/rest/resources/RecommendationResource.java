package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources;

import java.time.LocalDateTime;

public record RecommendationResource(
        Long id,
        Long userId,
        Long templateId,
        String reason,
        String notes,
        String timeOfDay,
        Double score,
        String status,
        LocalDateTime assignedAt
) {}