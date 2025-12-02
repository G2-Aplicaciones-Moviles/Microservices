package pe.edu.upc.recommendations_service.recommendations.domain.services;

import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.TimeOfDay;

public interface RecommendationMappingService {
    TimeOfDay determineTimeOfDay(RecommendationTemplate template);
    Double calculateScore(RecommendationTemplate template);
    String generateReason(RecommendationTemplate template);
}