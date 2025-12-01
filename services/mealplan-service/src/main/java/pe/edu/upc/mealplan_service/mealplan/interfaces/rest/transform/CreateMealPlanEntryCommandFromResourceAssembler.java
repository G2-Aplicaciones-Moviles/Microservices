package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanEntryCommand;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.CreateMealPlanEntryResource;

public class CreateMealPlanEntryCommandFromResourceAssembler {
    public static CreateMealPlanEntryCommand toCommandFromResource(int mealPlanId, CreateMealPlanEntryResource resource) {
        return new CreateMealPlanEntryCommand(
                mealPlanId,
                resource.recipeId(),
                resource.type(),
                resource.day()
        );
    }
}

