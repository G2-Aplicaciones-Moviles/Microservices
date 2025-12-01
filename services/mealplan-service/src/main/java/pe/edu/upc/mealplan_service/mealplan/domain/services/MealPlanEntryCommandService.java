package pe.edu.upc.mealplan_service.mealplan.domain.services;

import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanEntryCommand;

public interface MealPlanEntryCommandService {
    int handle(CreateMealPlanEntryCommand command);
}
