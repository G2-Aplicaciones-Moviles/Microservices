package pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;

import java.util.ArrayList;
import java.util.List;

@ToString
@Embeddable
public class MealPlanEntries {

    @Getter
    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanEntry> mealPlanEntries;

    public MealPlanEntries(){this.mealPlanEntries=new ArrayList<>();}

    public void addItem(MealPlanEntry mealPlanEntry) {
        if (mealPlanEntries == null) {
            mealPlanEntries = new ArrayList<>();
        }
        mealPlanEntries.add(mealPlanEntry);
    }
    public void addItems(List<MealPlanEntry> mealPlanEntries) {
        if (this.mealPlanEntries == null) {
            this.mealPlanEntries = new ArrayList<>();
        }
        this.mealPlanEntries.addAll(mealPlanEntries);
    }
    public void replaceWith(List<MealPlanEntry> newEntries) {
        this.mealPlanEntries.clear();
        this.mealPlanEntries.addAll(newEntries);
    }

}
