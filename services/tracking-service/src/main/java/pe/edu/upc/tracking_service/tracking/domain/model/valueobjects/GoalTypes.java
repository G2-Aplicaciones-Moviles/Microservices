package pe.edu.upc.tracking_service.tracking.domain.model.valueobjects;

import lombok.Getter;

@Getter
public enum GoalTypes {
    MANTENIMIENTO("Mantenimiento", 2000.0, 250.0, 125.0, 55.6),
    PERDIDA_PESO("Pérdida de peso", 1500.0, 150.0, 131.25, 41.7),
    GANANCIA_MUSCULAR("Ganancia muscular", 2500.0, 343.75, 156.25, 55.6);

    private final String displayName;
    private final double calories;
    private final double carbs;
    private final double proteins;
    private final double fats;

    GoalTypes(String displayName, double calories, double carbs, double proteins, double fats) {
        this.displayName = displayName;
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
    }
}