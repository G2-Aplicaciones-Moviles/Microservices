package pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

import java.util.Optional;

public interface TrackingGoalRepository extends JpaRepository<TrackingGoal, Long> {
    Optional<TrackingGoal> findByUserId(UserId userId);

    boolean existsByUserId(UserId userId);
    //Optional<MacronutrientValues> findTargetMacrosByTrackingGoalId(Long trackingGoalId);
}
