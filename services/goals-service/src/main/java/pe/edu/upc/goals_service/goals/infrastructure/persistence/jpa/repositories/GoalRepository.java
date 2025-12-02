package pe.edu.upc.goals_service.goals.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findByUserId_Value(Long userId);
}