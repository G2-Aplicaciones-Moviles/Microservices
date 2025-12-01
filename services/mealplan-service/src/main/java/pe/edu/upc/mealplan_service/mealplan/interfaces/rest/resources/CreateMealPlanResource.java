package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

import java.util.List;

public record CreateMealPlanResource(
        String name,
        String description,
        Float calories,
        Float carbs,
        Float proteins,
        Float fats,
        Integer profileId,
        String category,
        Boolean isCurrent,
        List<String> tags
) {
}
