package pe.edu.upc.mealplan_service.mealplan.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanTypes;


@Entity
@Table(name = "meal_plan_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class MealPlanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private MealPlanTypes type;

    public MealPlanType(MealPlanTypes type) {
        this.type = type;
    }
    public String getStringType() {
        return type.name();
    }

    public static MealPlanType getDefaultMealPlanType() {
        return new MealPlanType(MealPlanTypes.Breakfast);
    }

    public static MealPlanType toMealPlanTypeFromType(String type) {
        return new MealPlanType(MealPlanTypes.valueOf(type));
    }
}
