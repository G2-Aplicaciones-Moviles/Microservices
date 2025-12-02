package pe.edu.upc.goals_service.goals.domain.model.commands;

import pe.edu.upc.goals_service.goals.domain.model.valueobjects.ObjectiveType;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.PaceType;

public record UpdateGoalCaloriesCommand(
        Long userId,
        ObjectiveType objective,
        Double targetWeightKg,
        PaceType pace
) {}