package pe.edu.upc.mealplan_service.mealplan.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.RecipeId;

@Getter
@Entity
@Table(name = "meal_plan_entry")
public class MealPlanEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "recipeId", column = @Column(name = "recipe_id", nullable = false))
    })
    private RecipeId recipeId;

    @Column(name = "day", nullable = false)
    private int day;

    @ManyToOne
    @JoinColumn(name = "meal_plan_type_id", nullable = false)
    private MealPlanType mealPlanType;

    @ManyToOne
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    public MealPlanEntry() {
    }
    public MealPlanEntry(RecipeId recipeId, MealPlanType mealPlanType,int day, MealPlan mealPlan) {
        this.recipeId = recipeId;
        this.mealPlanType = mealPlanType;
        this.day = day;
        this.mealPlan = mealPlan;
    }
}
