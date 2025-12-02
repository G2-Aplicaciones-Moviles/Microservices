package pe.edu.upc.mealplan_service.mealplan.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.ExternalRecipeService;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanByProfileIdQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetEntriesWithRecipeInfo;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetMealPlanByIdQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetOriginalTemplatesQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanQueryService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanEntryRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanRepository;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanQueryServiceImpl implements MealPlanQueryService {
    private final MealPlanRepository mealPlanRepository;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final ExternalRecipeService externalRecipeService;

    public MealPlanQueryServiceImpl(MealPlanRepository mealPlanRepository,
                                    MealPlanEntryRepository mealPlanEntryRepository,
                                    ExternalRecipeService externalRecipeService) {
        this.mealPlanRepository = mealPlanRepository;
        this.externalRecipeService = externalRecipeService;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
    }
    @Override
    public Optional<MealPlan> handle(GetMealPlanByIdQuery query) {
        return this.mealPlanRepository.findById(query.mealPlanId());
    }

    public List<MealPlanEntryDetailedResource> handle(GetEntriesWithRecipeInfo query) {
        var entries = mealPlanEntryRepository.findAllByMealPlan_Id(query.mealPlanId());

        return entries.stream().map(entry -> {
            var recipeOpt = externalRecipeService.fetchRecipeById(entry.getRecipeId().recipeId());
            var recipe = recipeOpt.orElse(null);

            return new MealPlanEntryDetailedResource(
                    entry.getId(),
                    entry.getRecipeId().recipeId(),
                    recipe != null ? recipe.name() : null,
                    recipe != null ? recipe.description() : null,
                    entry.getDay(),
                    entry.getMealPlanType().getId(),
                    entry.getMealPlan().getId().intValue()
            );
        }).toList();
    }

    @Override
    public List<MealPlan> handle(GetAllMealPlanQuery query) {
        return this.mealPlanRepository.findAll();
    }

    @Override
    public List<MealPlan> handle(GetAllMealPlanByProfileIdQuery query) {
        return this.mealPlanRepository.findAllByProfileId_UserProfileId(query.ProfileId());
    }

    @Override
    public List<MealPlan> handle(GetOriginalTemplatesQuery query) {
        return this.mealPlanRepository
                .findAllByCreatedByNutritionistIdIsNotNullAndProfileId_UserProfileIdIsNull();
    }
}
