package pe.edu.upc.goals_service.goals.interfaces.rest.resources;

import pe.edu.upc.goals_service.goals.domain.model.valueobjects.DietPreset;

public record DietTypeConfigResource(
        DietPreset preset
) {}