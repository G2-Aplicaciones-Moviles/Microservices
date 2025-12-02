package pe.edu.upc.tracking_service.tracking.domain.model.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.MealPlanTypes;

@Entity(name = "Tracking_MealPlanType")
@Table(name = "mealplan_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class MealPlanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 12)
    private MealPlanTypes name;

    public MealPlanType(MealPlanTypes name) { this.name = name; }

    public String ToString() { return name.name(); }

    public static MealPlanType getDefaultMealPlanType() {
        return new MealPlanType(MealPlanTypes.HEALTHY);
    }
}
