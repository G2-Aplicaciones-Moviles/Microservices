package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.transform;

import pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates.Recommendation;
import pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources.RecommendationResource;

public class RecommendationResourceFromEntityAssembler {
    public static RecommendationResource toResourceFromEntity(Recommendation entity) {
        return new RecommendationResource(
                entity.getId(),
                entity.getUserId() != null ? entity.getUserId().getValue() : null,
                entity.getTemplate() != null ? entity.getTemplate().getId() : null,
                entity.getReason(),
                entity.getNotes(),
                entity.getTimeOfDay() != null ? entity.getTimeOfDay().name() : null,
                entity.getScore(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getAssignedAt()
        );
    }
}