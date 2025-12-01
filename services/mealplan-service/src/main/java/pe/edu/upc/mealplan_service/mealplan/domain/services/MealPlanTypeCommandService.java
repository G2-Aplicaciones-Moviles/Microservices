package pe.edu.upc.mealplan_service.mealplan.domain.services;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.SeedMealPlanTypesCommand;

public interface MealPlanTypeCommandService {
    void handle(SeedMealPlanTypesCommand command);
}
