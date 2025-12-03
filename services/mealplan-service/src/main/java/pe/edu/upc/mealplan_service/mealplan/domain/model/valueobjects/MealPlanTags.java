package pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;

import java.util.ArrayList;
import java.util.List;

@ToString
@Embeddable
public class MealPlanTags {
    @Getter
    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanTag> mealPlanTags;

    public MealPlanTags() {
        this.mealPlanTags = new ArrayList<>();
    }

    public void addItem(MealPlanTag mealPlanTag) {
        this.mealPlanTags.add(mealPlanTag);
    }
    public void addItems(List<MealPlanTag> mealPlanTags) {
        this.mealPlanTags.addAll(mealPlanTags);
    }

    public void replaceWith(List<MealPlanTag> newTags) {
        this.mealPlanTags.clear();
        this.mealPlanTags.addAll(newTags);
    }

}
