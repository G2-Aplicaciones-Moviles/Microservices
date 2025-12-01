package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryResource;

import java.util.List;

public class MealPlanEntryResourceFromEntityAssembler {
    public static List<MealPlanEntryResource> toResourceFromEntities(List<MealPlanEntry> entities) {
        return entities.stream()
                .map(entity -> new MealPlanEntryResource(
                        entity.getId(),
                        entity.getRecipeId().recipeId(),
                        entity.getDay(),
                        entity.getMealPlanType().getId(),
                        entity.getMealPlan().getId()
                ))
                .toList();
    }
}
