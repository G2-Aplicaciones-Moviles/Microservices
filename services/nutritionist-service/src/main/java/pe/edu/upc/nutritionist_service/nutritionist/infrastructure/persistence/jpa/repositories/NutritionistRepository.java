package pe.edu.upc.nutritionist_service.nutritionist.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;

import java.util.Optional;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, Long> {
    Optional<Nutritionist> findByUserId(Long userId);
}
