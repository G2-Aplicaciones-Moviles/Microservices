package pe.edu.upc.goals_service.goals.domain.services;

import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.domain.model.queries.GetGoalByUserQuery;

import java.util.Optional;

public interface GoalQueryService {
    Optional<Goal> handle(GetGoalByUserQuery query);
}
