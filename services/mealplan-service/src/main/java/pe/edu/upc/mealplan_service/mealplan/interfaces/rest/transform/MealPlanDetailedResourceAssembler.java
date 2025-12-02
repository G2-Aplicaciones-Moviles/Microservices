package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanDetailedResource;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeResource;

import java.util.function.Function;

public class MealPlanDetailedResourceAssembler {

    public static MealPlanDetailedResource toResourceFromEntity(
            MealPlan mealPlan,
            Function<Integer, RecipeResource> fetchRecipe
    ) {
        return new MealPlanDetailedResource(
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
                MealPlanEntryDetailedResourceAssembler.toDetailedResourcesFromEntities(
                        mealPlan.getEntries().getMealPlanEntries(), fetchRecipe),
                mealPlan.getTags().getMealPlanTags().stream()
                        .map(MealPlanTag::getTag)
                        .toList()
        );
    }
}
