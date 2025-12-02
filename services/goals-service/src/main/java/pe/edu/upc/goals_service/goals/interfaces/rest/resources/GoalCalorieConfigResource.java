package pe.edu.upc.goals_service.goals.interfaces.rest.resources;

import pe.edu.upc.goals_service.goals.domain.model.valueobjects.ObjectiveType;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.PaceType;

public record GoalCalorieConfigResource(
        ObjectiveType objective,
        Double targetWeightKg,
        PaceType pace
) {}