package pe.edu.upc.goals_service.goals.interfaces.rest.resources;

import pe.edu.upc.goals_service.goals.domain.model.valueobjects.DietPreset;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.ObjectiveType;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.PaceType;

public record GoalResource (
    Long userId,
    ObjectiveType objective,
    Double targetWeightKg,
    PaceType pace,
    DietPreset dietPreset,
    Integer proteinPct,
    Integer carbsPct,
    Integer fatPct
) {}