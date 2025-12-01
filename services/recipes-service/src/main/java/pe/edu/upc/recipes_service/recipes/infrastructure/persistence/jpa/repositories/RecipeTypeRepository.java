package pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;

import java.util.Optional;

@Repository
public interface RecipeTypeRepository extends JpaRepository<RecipeType, Long> {
    Optional<RecipeType> findByName(String name);
    boolean existsByName(String name);
}