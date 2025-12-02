package pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Allergy;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    /**
     * Busca una alergia por su nombre exacto.
     */
    Optional<Allergy> findByName(String name);



    /**
     * Busca alergias que incluyan un ingrediente específico.
     */
    List<Allergy> findByRelatedIngredients_Name(String ingredientName);
}
