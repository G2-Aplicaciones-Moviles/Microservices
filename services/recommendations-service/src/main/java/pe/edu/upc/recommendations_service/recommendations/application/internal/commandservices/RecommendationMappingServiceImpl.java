package pe.edu.upc.recommendations_service.recommendations.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.TimeOfDay;
import pe.edu.upc.recommendations_service.recommendations.domain.services.RecommendationMappingService;

@Service
public class RecommendationMappingServiceImpl implements RecommendationMappingService {

    @Override
    public TimeOfDay determineTimeOfDay(RecommendationTemplate template) {
        return switch (template.getCategory().toLowerCase()) {
            case "hydration" -> TimeOfDay.MORNING;
            case "nutrition" -> TimeOfDay.AFTERNOON;
            case "exercise" -> TimeOfDay.EVENING;
            case "sleep" -> TimeOfDay.ALL_DAY;
            default -> TimeOfDay.values()[Math.abs(template.getTitle().hashCode()) % TimeOfDay.values().length];
        };
    }

    @Override
    public Double calculateScore(RecommendationTemplate template) {
        return switch (template.getCategory().toLowerCase()) {
            case "hydration" -> 8.0;
            case "nutrition" -> 7.5;
            case "exercise" -> 8.5;
            case "sleep" -> 9.0;
            default -> 7.0 + (Math.abs(template.getTitle().hashCode()) % 3);
        };
    }

    @Override
    public String generateReason(RecommendationTemplate template) {
        return "Recomendación de " + template.getCategory() + ": " + template.getTitle();
    }
}