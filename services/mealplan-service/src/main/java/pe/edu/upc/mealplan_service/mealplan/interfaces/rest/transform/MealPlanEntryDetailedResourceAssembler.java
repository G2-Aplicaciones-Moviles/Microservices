package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.RecipeResource;

import java.util.List;

public class MealPlanEntryDetailedResourceAssembler {

    public static List<MealPlanEntryDetailedResource> toDetailedResourcesFromEntities(
            List<MealPlanEntry> entries,
            java.util.function.Function<Integer, RecipeResource> fetchRecipe
    ) {
        return entries.stream().map(entry -> {
            var recipe = fetchRecipe.apply(entry.getRecipeId().recipeId());
            return new MealPlanEntryDetailedResource(
                    entry.getId(),
                    entry.getRecipeId().recipeId(),
                    recipe.name(),
                    recipe.description(),
                    entry.getDay(),
                    entry.getMealPlanType().getId(),
                    entry.getMealPlan().getId().intValue()
            );
        }).toList();
    }
}


