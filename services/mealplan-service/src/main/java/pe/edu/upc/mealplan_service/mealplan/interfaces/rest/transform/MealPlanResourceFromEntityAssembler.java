package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanResource;

public class MealPlanResourceFromEntityAssembler {

    public static MealPlanResource toResourceFromEntity(MealPlan mealPlan) {
        return new MealPlanResource(
                mealPlan.getId().intValue(),
                mealPlan.getName(),
                mealPlan.getDescription(),
                mealPlan.getMacros().getCalories(),
                mealPlan.getMacros().getCarbs(),
                mealPlan.getMacros().getProteins(),
                mealPlan.getMacros().getFats(),
                mealPlan.getProfileId().userProfileId(),
                mealPlan.getCategory(),
                mealPlan.getIsCurrent(),
                MealPlanEntryResourceFromEntityAssembler.toResourceFromEntities(
                        mealPlan.getEntries().getMealPlanEntries()),
                mealPlan.getTags().getMealPlanTags()
                        .stream()
                        .map(MealPlanTag::getTag)
                        .toList()
        );
    }
}
