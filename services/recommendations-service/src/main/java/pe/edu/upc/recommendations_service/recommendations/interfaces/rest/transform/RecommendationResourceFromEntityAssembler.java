// src/main/java/pe/edu/upc/center/backendNutriSmart/recommendations/interfaces/rest/transform/RecommendationResourceFromEntityAssembler.java

package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.recommendations.domain.model.aggregates.Recommendation;
import pe.edu.upc.center.jameoFit.recommendations.interfaces.rest.resources.RecommendationResource;

public class RecommendationResourceFromEntityAssembler {
    public static RecommendationResource toResourceFromEntity(Recommendation entity) {
        return new RecommendationResource(
                (long) entity.getId(),
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