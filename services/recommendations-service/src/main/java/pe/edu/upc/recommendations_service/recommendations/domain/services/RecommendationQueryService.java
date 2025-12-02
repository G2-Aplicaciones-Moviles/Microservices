package pe.edu.upc.recommendations_service.recommendations.domain.services;

import pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates.Recommendation;
import pe.edu.upc.recommendations_service.recommendations.domain.model.queries.GetRecommendationsByUserQuery;

import java.util.List;

public interface RecommendationQueryService {
    List<Recommendation> handle(GetRecommendationsByUserQuery query);
}
