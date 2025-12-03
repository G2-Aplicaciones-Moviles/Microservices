package pe.edu.upc.tracking_service.tracking.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.edu.upc.tracking_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.MealPlanEntries;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name = "tracking")
@ToString
public class Tracking extends AuditableAbstractAggregateRoot<Tracking> {

    @Column(name = "tracking_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Embedded
    private UserId userId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tracking_goal_id", nullable = false)
    private TrackingGoal trackingGoal;

    /**
     * -- SETTER --
     * Setter directo para consumed macros (útil desde services que calculan y persisten un nuevo MacronutrientValues).
     */
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "consumed_macros_id", referencedColumnName = "id")
    private MacronutrientValues consumedMacros;

    @Embedded
    private MealPlanEntries mealPlanEntries = new MealPlanEntries();

    @PostLoad
    private void initMealPlanEntriesIfNull() {
        if (mealPlanEntries == null) {
            mealPlanEntries = new MealPlanEntries();
        }
    }

    // Constructor por defecto
    public Tracking() {
        this.mealPlanEntries = new MealPlanEntries();
    }

    // Constructor con parámetros básicos
    public Tracking(UserId userId, LocalDate date, TrackingGoal trackingGoal) {
        this();
        this.userId = userId;
        this.date = date;
        this.trackingGoal = trackingGoal;
    }

    // Constructor con macros consumidos
    public Tracking(UserId userId, LocalDate date, TrackingGoal trackingGoal, MacronutrientValues consumedMacros) {
        this();
        this.userId = userId;
        this.date = date;
        this.trackingGoal = trackingGoal;
        this.consumedMacros = consumedMacros;
    }

    // Métodos de negocio para gestionar las entradas del plan de comidas
    public void addMealPlanEntry(TrackingMealPlanEntry mealPlanEntry) {
        this.mealPlanEntries.addEntry(mealPlanEntry);
    }

    public void addMealPlanEntries(List<TrackingMealPlanEntry> entries) {
        this.mealPlanEntries.addEntries(entries);
    }

    public boolean removeMealPlanEntry(TrackingMealPlanEntry mealPlanEntry) {
        return mealPlanEntries.removeEntryById(mealPlanEntry.getId());
    }

    public void updateMealPlanEntry(TrackingMealPlanEntry oldEntry, TrackingMealPlanEntry newEntry) {
        this.mealPlanEntries.removeEntryById(oldEntry.getId());
        this.mealPlanEntries.addEntry(newEntry);
    }

    /**
     * Agrega (suma) macronutrientes consumidos.
     * Nota: este método solo actualiza el estado en memoria; el service debe persistir el MacronutrientValues resultante y guardar el aggregate.
     */
    public void addConsumedMacros(MacronutrientValues addition) {
        if (addition == null) return;
        if (this.consumedMacros == null) {
            this.consumedMacros = addition;
        } else {
            double newCalories = this.consumedMacros.getCalories() + addition.getCalories();
            double newCarbs = this.consumedMacros.getCarbs() + addition.getCarbs();
            double newProteins = this.consumedMacros.getProteins() + addition.getProteins();
            double newFats = this.consumedMacros.getFats() + addition.getFats();
            this.consumedMacros = new MacronutrientValues(newCalories, newCarbs, newProteins, newFats);
        }
    }
}
