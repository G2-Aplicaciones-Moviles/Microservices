package pe.edu.upc.goals_service.goals.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.domain.model.queries.GetGoalByUserQuery;
import pe.edu.upc.goals_service.goals.domain.services.GoalQueryService;
import pe.edu.upc.goals_service.goals.infrastructure.persistence.jpa.repositories.GoalRepository;

import java.util.Optional;

@Service
public class GoalQueryServiceImpl implements GoalQueryService {

    private final GoalRepository repository;

    public GoalQueryServiceImpl(GoalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Goal> handle(GetGoalByUserQuery query) {
        return repository.findByUserId_Value(query.userId());
    }
}