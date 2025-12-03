package pe.edu.upc.mealplan_service.mealplan.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanType;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanTypesQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetMealPlanTypeByNameQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanTypeQueryService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanTypeQueryServiceImpl implements MealPlanTypeQueryService {

    private final MealPlanTypeRepository mealPlanTypeRepository;

    public MealPlanTypeQueryServiceImpl(MealPlanTypeRepository mealPlanTypeRepository) {
        this.mealPlanTypeRepository = mealPlanTypeRepository;
    }

    @Override
    public List<MealPlanType> handle(GetAllMealPlanTypesQuery query) {
        return mealPlanTypeRepository.findAll();
    }

    @Override
    public Optional<MealPlanType> handle(GetMealPlanTypeByNameQuery query) {
        return mealPlanTypeRepository.findByType(query.type());
    }
}

