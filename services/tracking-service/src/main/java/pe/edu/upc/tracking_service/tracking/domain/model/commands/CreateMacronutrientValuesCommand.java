package pe.edu.upc.tracking_service.tracking.domain.model.commands;

public record CreateMacronutrientValuesCommand(Long id, double calories, double carbs, double proteins, double fats) {
}
