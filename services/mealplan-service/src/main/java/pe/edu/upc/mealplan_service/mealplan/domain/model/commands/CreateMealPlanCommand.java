package pe.edu.upc.mealplan_service.mealplan.domain.model.commands;

import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;

import java.util.List;

public record CreateMealPlanCommand(
        String name,
        String description,
        MealPlanMacros macros,
        UserProfileId profileId,
        String category,
        Boolean isCurrent,
        List<String> tags
) {
}
