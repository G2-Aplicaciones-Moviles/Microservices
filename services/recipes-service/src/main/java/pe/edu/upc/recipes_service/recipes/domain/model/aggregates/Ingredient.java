package pe.edu.upc.recipes_service.recipes.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.MacronutrientValuesId;
import pe.edu.upc.recipes_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
@Table(name = "ingredients")
@ToString
@NoArgsConstructor
public class Ingredient extends AuditableAbstractAggregateRoot<Ingredient> {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "calories", nullable = false)
    private double calories;

    @Column(name = "carbohydrates", nullable = false)
    private double carbohydrates;

    @Column(name = "proteins", nullable = false)
    private double proteins;

    @Column(name = "fats", nullable = false)
    private double fats;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "macronutrientValuesId",
                    column = @Column(name = "macronutrient_values_id", nullable = false))
    })
    private MacronutrientValuesId macronutrientValuesId = new MacronutrientValuesId();

    public Ingredient(String name, double calories, double carbohydrates, double proteins, double fats, Long macronutrientValuesId) {
        this.name = name;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.macronutrientValuesId = new MacronutrientValuesId(macronutrientValuesId);
    }

    // Getter explícito para el value object
    public Long getMacronutrientValuesId() {
        return this.macronutrientValuesId.macronutrientValuesId();
    }
}
