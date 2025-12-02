package pe.edu.upc.goals_service.goals.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.*;
import pe.edu.upc.goals_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "goals",
        uniqueConstraints = { @UniqueConstraint(name = "uk_goals_user", columnNames = "user_id") })
public class Goal extends AuditableAbstractAggregateRoot<Goal> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    })
    private UserId userId;

    // Sección 1: objetivo y calorías
    @Enumerated(EnumType.STRING)
    @Column(name = "objective", nullable = false)
    private ObjectiveType objective;

    @DecimalMin(value = "1.0", inclusive = true, message = "Target weight must be > 0")
    @Column(name = "target_weight_kg", nullable = false)
    private Double targetWeightKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "pace", nullable = false)
    private PaceType pace;

    // Sección 2: tipo de dieta (macros fijos por preset)
    @Enumerated(EnumType.STRING)
    @Column(name = "diet_preset", nullable = false)
    private DietPreset dietPreset;

    // (Opcional) cache de macros sugeridos por preset — solo lectura/guía
    @Column(name = "protein_pct")
    private Integer proteinPct;

    @Column(name = "carbs_pct")
    private Integer carbsPct;

    @Column(name = "fat_pct")
    private Integer fatPct;

    @Temporal(TemporalType.TIMESTAMP)     // <-- anota Date con @Temporal
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public Goal(UserId userId, ObjectiveType objective, Double targetWeightKg, PaceType pace, DietPreset dietPreset) {
        this.userId = userId;
        this.objective = objective;
        this.targetWeightKg = targetWeightKg;
        this.pace = pace;
        this.dietPreset = dietPreset;
        this.updatedAt = new Date();
    }

    public void updateGoalCalories(ObjectiveType objective, Double targetWeightKg, PaceType pace) {
        this.objective = objective;
        this.targetWeightKg = targetWeightKg;
        this.pace = pace;
        this.updatedAt = new Date();
    }

    public void updateDietPreset(DietPreset preset, Integer p, Integer c, Integer f) {
        this.dietPreset = preset;
        this.proteinPct = p; this.carbsPct = c; this.fatPct = f;
        this.updatedAt = new Date();
    }
}