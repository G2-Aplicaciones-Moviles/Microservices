package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

import java.util.List;

public record MealPlanResource(
        int id,
        String name,
        String description,
        Double calories,
        Double carbs,
        Double proteins,
        Double fats,
        Integer profileId,
        String category,
        Boolean isCurrent,
        List<MealPlanEntryResource> entries,
        List<String> tags
) {
}
