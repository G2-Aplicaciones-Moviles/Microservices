package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.CreateMealPlanResource;

public class CreateMealPlanCommandFromResourceAssembler {

    public static CreateMealPlanCommand toCommandFromResource(CreateMealPlanResource resource) {
        return new CreateMealPlanCommand(
                resource.name(),
                resource.description(),
                new MealPlanMacros(
                        resource.calories(),
                        resource.carbs(),
                        resource.proteins(),
                        resource.fats()
                ),
                new UserProfileId(resource.profileId()),
                resource.category(),
                resource.isCurrent(),
                resource.tags()
        );
    }
}
