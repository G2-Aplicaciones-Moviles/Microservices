package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;
//para post
public record CreateTrackingGoalResource(
        Long userId,
        CreateMacronutrientValuesResource targetMacros
) {
}
