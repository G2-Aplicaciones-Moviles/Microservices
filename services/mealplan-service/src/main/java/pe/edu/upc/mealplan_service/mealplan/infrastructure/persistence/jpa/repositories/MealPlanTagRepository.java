package pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;

@Repository
public interface MealPlanTagRepository extends JpaRepository<MealPlanTag, Integer> {
}
