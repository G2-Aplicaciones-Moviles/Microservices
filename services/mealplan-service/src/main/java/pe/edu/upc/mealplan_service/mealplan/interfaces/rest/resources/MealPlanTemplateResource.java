package pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources;

public record MealPlanTemplateResource(
        int id,
        String name,
        String description,
        String category,
        Integer createdByNutritionistId,
        String nutritionistName,
        double calories,
        double carbs,
        double proteins,
        double fats
) {
}

