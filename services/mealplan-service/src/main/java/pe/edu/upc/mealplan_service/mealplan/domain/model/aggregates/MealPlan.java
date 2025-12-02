package pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanEntries;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanTags;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.mealplan_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Setter
@Entity
@Table(name = "meal_plans")
public class MealPlan extends AuditableAbstractAggregateRoot<MealPlan> {


    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calories", column = @Column(name = "total_calories")),
            @AttributeOverride(name = "carbs", column = @Column(name = "total_carbs")),
            @AttributeOverride(name = "proteins", column = @Column(name = "total_proteins")),
            @AttributeOverride(name = "fats", column = @Column(name = "total_fats"))
    })
    private MealPlanMacros macros;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "userProfileId",
                    column = @Column(name = "profile_id", nullable = true)
            )
    })
    private UserProfileId profileId;

    @Embedded
    private MealPlanEntries entries;

    @Embedded
    private MealPlanTags tags;

    @Column(name = "category", length = 100, nullable = false)
    private String category;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent;

    @Column(name = "created_by_nutritionist_user_id", nullable = true)
    private Integer createdByNutritionistId;


    public MealPlan() {}

    public MealPlan(CreateMealPlanCommand command) {
        this.name = command.name();
        this.description = command.description();
        this.macros = command.macros();
        this.profileId = command.profileId();
        this.entries = new MealPlanEntries();
        this.tags = new MealPlanTags();
        this.category = command.category();
        this.isCurrent = command.isCurrent() == null ? false : command.isCurrent();
        this.createdByNutritionistId = command.createdByNutritionistId();

        if (command.tags() != null) {
            for (String t : command.tags()) {
                this.addTag(new MealPlanTag(t));
            }
        }
    }
    public void recalculateMacros() {
//        int totalCalories = 0, totalCarbs = 0, totalProteins = 0, totalFats = 0;
//
//        for (MealPlanEntry meal : mealPlanEntries) {
//            var macros = meal.getRecipe().getMacros();
//            totalCalories += macros.totalCalories();
//            totalCarbs += macros.totalCarbs();
//            totalProteins += macros.totalProteins();
//            totalFats += macros.totalFats();
//        }
//
//        this.totalMacros = new Macronutrients(totalCalories, totalCarbs, totalProteins, totalFats);
    }
//    public void updateMeal(int dayNumber, MealPlanType type, Recipe newRecipe) {
//        meals.stream()
//                .filter(meal -> meal.getDayNumber() == dayNumber && meal.getType().equals(type))
//                .findFirst()
//                .ifPresent(meal -> meal.setRecipe(newRecipe));
//
//        recalculateMacros();
//    }
    public void assignAsCurrent(){};
    public void unassignAsCurrent(){};
    public void addEntry(MealPlanEntry entry) {
        if (this.entries == null) this.entries = new MealPlanEntries();
        this.entries.getMealPlanEntries().add(entry);
    }

    public void addTag(MealPlanTag tag) {
        if (this.tags == null) this.tags = new MealPlanTags();
        this.tags.getMealPlanTags().add(tag);
    }

    public void addNutrition(double kc, double c, double p, double f) {
        if (this.getMacros() == null) this.setMacros(new MealPlanMacros(0.0,0.0,0.0,0.0));
        this.setMacros(this.getMacros().plus(kc, c, p, f));
    }
//    public void addMeal(DayNumber day, MealPlanType type, RecipeReference recipe);
//    public void removeMeal(DayNumber day, MealPlanType type);
//
//    public void replaceMealRecipe(DayNumber day, MealPlanType type, RecipeReference old, RecipeReference updated);

    public MealPlan(String name, String description, String dietaryRequirements) {
        this.name = name;
        this.description = description;
    }

}
