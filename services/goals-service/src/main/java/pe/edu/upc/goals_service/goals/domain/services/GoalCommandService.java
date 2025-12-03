package pe.edu.upc.goals_service.goals.domain.services;

import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateDietTypeCommand;
import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateGoalCaloriesCommand;

public interface GoalCommandService {
    Goal handle(UpdateGoalCaloriesCommand command);
    Goal handle(UpdateDietTypeCommand command);
}
