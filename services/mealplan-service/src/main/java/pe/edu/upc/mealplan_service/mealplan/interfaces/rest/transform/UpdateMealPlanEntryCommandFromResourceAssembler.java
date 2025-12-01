package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.UpdateMealPlanEntryCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.RecipeId;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryResource;

public class UpdateMealPlanEntryCommandFromResourceAssembler {
    public static UpdateMealPlanEntryCommand toCommandFromResource(MealPlanEntryResource resource) {
        return new UpdateMealPlanEntryCommand(
                resource.id(),
                new RecipeId(resource.recipeId()),
                resource.day(),           // ← primero day
                resource.mealPlanType(),  // ← luego mealPlanTypeId
                resource.mealPlanId()
        );
    }
}
