package pe.edu.upc.mealplan_service.mealplan.domain.model.commands;

public record CreateMealPlanEntryCommand(
        int mealPlanId,
        int recipeId,
        String type, // "Breakfast"|"Lunch"|"Dinner"|"Snack"
        int day
) {
}


