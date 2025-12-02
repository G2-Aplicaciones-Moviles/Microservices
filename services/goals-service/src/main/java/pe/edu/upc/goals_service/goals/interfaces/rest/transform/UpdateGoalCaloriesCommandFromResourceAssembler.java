package pe.edu.upc.goals_service.goals.interfaces.rest.transform;

import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateGoalCaloriesCommand;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.GoalCalorieConfigResource;

public class UpdateGoalCaloriesCommandFromResourceAssembler {
    public static UpdateGoalCaloriesCommand toCommand(Long userId, GoalCalorieConfigResource r) {
        return new UpdateGoalCaloriesCommand(userId, r.objective(), r.targetWeightKg(), r.pace());
    }
}