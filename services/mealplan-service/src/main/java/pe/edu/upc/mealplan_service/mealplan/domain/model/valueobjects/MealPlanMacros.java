package pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class MealPlanMacros {
    private double calories;
    private double carbs;
    private double proteins;
    private double fats;

    public MealPlanMacros(double calories, double carbs, double proteins, double fats) {
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
    }

    public MealPlanMacros plus(double kc, double c, double p, double f) {
        return new MealPlanMacros(
                this.calories + kc,
                this.carbs + c,
                this.proteins + p,
                this.fats + f
        );
    }
}