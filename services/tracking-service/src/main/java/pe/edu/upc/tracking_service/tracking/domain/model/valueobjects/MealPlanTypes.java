package pe.edu.upc.tracking_service.tracking.domain.model.valueobjects;

public enum MealPlanTypes {
  BREAKFAST (1),
  LUNCH (2),
  DINNER (3),
  HEALTHY(4);

  private final int value;

  MealPlanTypes(int value) {this.value= value; }
}
