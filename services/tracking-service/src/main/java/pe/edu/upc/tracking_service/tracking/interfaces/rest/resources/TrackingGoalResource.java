package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;
//para get y put
public record TrackingGoalResource(
        Long id,
        Long userId,
        MacronutrientValuesResource targetMacros
){ }
