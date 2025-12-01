package pe.edu.upc.tracking_service.tracking.interfaces.rest.resources;
//para post
public record CreateMacronutrientValuesResource(
        double calories,
        double carbs,
        double proteins,
        double fats
) {
}