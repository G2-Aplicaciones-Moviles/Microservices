package pe.edu.upc.tracking_service.tracking.domain.model.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "macronutrient_values")
public class MacronutrientValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "calories")
    private double calories;

    @NotNull
    @Column(name = "carbs")
    private double carbs;

    @NotNull
    @Column(name = "proteins")
    private double proteins;

    @NotNull
    @Column(name = "fats")
    private double fats;

    public MacronutrientValues(double calories, double carbs, double proteins, double fats) {
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
    }

    public MacronutrientValues() {

    }
}
