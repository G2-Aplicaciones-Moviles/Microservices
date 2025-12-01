package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

public record ProgressResource(
        MacroProgressResource calories,
        MacroProgressResource carbs,
        MacroProgressResource proteins,
        MacroProgressResource fats
) { }
