package pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;

public interface MacronutrientValuesRepository extends JpaRepository<MacronutrientValues, Long> {
    //Consumed Macros
    //Target Macros
    //Optional<MacronutrientValues> findTargetMacrosByTrackingGoalId(@Param("trackingGoalId") Long trackingGoalId);

    boolean existsByCaloriesAndCarbsAndProteinsAndFats(double calories, double carbs, double proteins, double fats);
}
