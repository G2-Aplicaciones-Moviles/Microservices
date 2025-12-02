package pe.edu.upc.mealplan_service.mealplan.domain.services;

import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanByProfileIdQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetEntriesWithRecipeInfo;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetMealPlanByIdQuery;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying meal plans.
 */
public interface MealPlanQueryService {

    Optional<MealPlan> handle(GetMealPlanByIdQuery query);

    List<MealPlan> handle(GetAllMealPlanQuery query);

    List<MealPlan> handle(GetAllMealPlanByProfileIdQuery query);

    List<MealPlanEntryDetailedResource> handle(GetEntriesWithRecipeInfo query);
}

