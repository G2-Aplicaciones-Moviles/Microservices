package pe.edu.upc.recommendations_service.recommendations.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates.Recommendation;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findAllByUserId_Value(Long userId);

    List<Recommendation> findByTemplateAndUserIdIsNull(RecommendationTemplate template);
    Optional<Recommendation> findFirstByTemplateAndUserIdIsNull(RecommendationTemplate template);

    @Query("SELECT r FROM Recommendation r WHERE r.userId IS NULL")
    List<Recommendation> findByUserIdIsNull();

    @Query("SELECT COUNT(r) FROM Recommendation r WHERE r.userId IS NULL")
    Long countAvailableBaseRecommendations();
}