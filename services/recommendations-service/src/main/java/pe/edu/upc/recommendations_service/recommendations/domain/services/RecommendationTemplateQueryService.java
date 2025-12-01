package pe.edu.upc.recommendations_service.recommendations.domain.services;

import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;

import java.util.List;

public interface RecommendationTemplateQueryService {
    List<RecommendationTemplate> getAllTemplates();
}