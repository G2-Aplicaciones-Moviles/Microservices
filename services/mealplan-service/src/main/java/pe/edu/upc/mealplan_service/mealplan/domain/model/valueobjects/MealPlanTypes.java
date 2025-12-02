package pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects;

public enum MealPlanTypes {
    Breakfast (1),
    Lunch (2),
    Dinner (3),
    Snack (4);

    private final int value;

    MealPlanTypes(int value) {
        this.value = value;
    }
}
