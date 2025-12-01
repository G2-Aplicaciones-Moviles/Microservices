package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.UpdateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanResource;

public class UpdateMealPlanCommandFromResourceAssembler {
    public static UpdateMealPlanCommand toCommandFromResource(MealPlanResource resource, int mealPlanId) {
        return new UpdateMealPlanCommand(
            mealPlanId,
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
                resource.entries().stream()
                        .map(UpdateMealPlanEntryCommandFromResourceAssembler::toCommandFromResource)
                        .toList(),
                resource.tags()
        );
    }
}
