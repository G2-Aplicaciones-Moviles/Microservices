package pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;

import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Integer> {
    List<MealPlan> findAllByProfileId_UserProfileId(Integer userProfileId);

    List<MealPlan> findAllByCreatedByNutritionistIdIsNotNullAndProfileId_UserProfileIdIsNull();
}