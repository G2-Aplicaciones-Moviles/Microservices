package pe.edu.upc.recommendations_service.recommendations.application.internal.queryservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.domain.services.RecommendationTemplateService;
import pe.edu.upc.recommendations_service.recommendations.infrastructure.persistence.jpa.repositories.RecommendationTemplateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationTemplateServiceImpl implements RecommendationTemplateService {

    @Autowired
    private RecommendationTemplateRepository repository;

    @Override
    public List<RecommendationTemplate> getAllTemplates() {
        return repository.findAll();
    }

    @Override
    public Optional<RecommendationTemplate> findById(Long id) {
        return repository.findById(id);
    }
}