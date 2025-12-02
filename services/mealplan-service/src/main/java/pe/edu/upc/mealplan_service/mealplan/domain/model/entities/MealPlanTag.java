package pe.edu.upc.mealplan_service.mealplan.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;

@Getter
@Entity
@Table(name = "meal_plan_tags")
public class MealPlanTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "tag", length = 50, nullable = false)
    private String tag;

    @Setter
    @ManyToOne
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    public MealPlanTag(String tag) {
        this.tag = tag;
    }

    public MealPlanTag() {
    }

    public MealPlanTag(String tag, MealPlan mealPlan) {
        this.tag = tag;
        this.mealPlan = mealPlan;
    }
}
