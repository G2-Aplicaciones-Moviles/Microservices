package pe.edu.upc.goals_service.goals.domain.model.commands;

import pe.edu.upc.goals_service.goals.domain.model.valueobjects.DietPreset;

public record UpdateDietTypeCommand(
        Long userId,
        DietPreset preset
) {}