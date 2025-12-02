package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;

public record MacroProgressResource(
        double consumed,
        double target,
        double percent
) { }
