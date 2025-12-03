package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.CreateMealPlanResource;

public class CreateMealPlanCommandFromResourceAssembler {

    /**
     * Convierte un resource a command (para usuarios creando sus propios meal plans).
     */
    public static CreateMealPlanCommand toCommandFromResource(CreateMealPlanResource resource) {
        return new CreateMealPlanCommand.Builder()
                .name(resource.name())
                .description(resource.description())
                .macros(new MealPlanMacros(
                        resource.calories(),
                        resource.carbs(),
                        resource.proteins(),
                        resource.fats()
                ))
                .profileId(resource.profileId())
                .category(resource.category())
                .isCurrent(resource.isCurrent())
                .tags(resource.tags())
                .createdByNutritionistId(resource.createdByNutritionistId())
                .build();
    }

    /**
     * Sobrecarga para construcción con nutritionistUserId explícito.
     * Usado cuando un nutricionista crea un template.
     */
    public static CreateMealPlanCommand toCommandFromResource(CreateMealPlanResource resource, Long nutritionistUserId) {
        return new CreateMealPlanCommand.Builder()
                .name(resource.name())
                .description(resource.description())
                .macros(new MealPlanMacros(
                        resource.calories(),
                        resource.carbs(),
                        resource.proteins(),
                        resource.fats()
                ))
                .profileId(resource.profileId())
                .category(resource.category())
                .isCurrent(resource.isCurrent())
                .tags(resource.tags())
                .createdByNutritionistId(nutritionistUserId)
                .build();
    }
}
