package pe.edu.upc.recommendations_service.recommendations.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.infrastructure.persistence.jpa.repositories.RecommendationTemplateRepository;

import java.util.List;

@Service
public class RecommendationTemplateQueryService implements pe.edu.upc.recommendations_service.recommendations.domain.services.RecommendationTemplateQueryService {

    private final RecommendationTemplateRepository repository;

    public RecommendationTemplateQueryService(RecommendationTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RecommendationTemplate> getAllTemplates() {
        return repository.findAll();
    }
}
