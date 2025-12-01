package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources;

import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.RecommendationStatus;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.TimeOfDay;

public record UpdateRecommendationResource(
        String reason,
        String notes,
        TimeOfDay timeOfDay,
        Double score,
        RecommendationStatus status
) {
}
