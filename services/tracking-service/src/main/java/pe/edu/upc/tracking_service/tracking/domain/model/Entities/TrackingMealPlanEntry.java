package pe.edu.upc.tracking_service.tracking.domain.model.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.tracking_service.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.tracking_service.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.RecipeId;

@Getter
@Setter
@Entity
@Table(name = "tracking_meal_plan_entry")
public class TrackingMealPlanEntry extends AuditableModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Embedded
  private RecipeId recipeId;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "mealplan_type")
  private MealPlanType mealPlanType;


  @Min(1)
  @Max(7)
  @Column(name = "day_number")
  private int dayNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tracking_id", nullable = false)
  private Tracking tracking;


  public TrackingMealPlanEntry() {

  }

  public TrackingMealPlanEntry(RecipeId recipeId, MealPlanType mealPlanType, int dayNumber) {
    this.recipeId = recipeId;
    this.mealPlanType = mealPlanType;
    this.dayNumber = dayNumber;
  }


}
