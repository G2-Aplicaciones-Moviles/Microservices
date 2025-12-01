package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

import java.util.List;

public record MealPlanDetailedResource(
        int id,
        String name,
        String description,
        double  calories,
        double   carbs,
        double   proteins,
        double fats,
        int profileId,
        String category,
        boolean isCurrent,
        List<MealPlanEntryDetailedResource> entries,
        List<String> tags
) {
}
