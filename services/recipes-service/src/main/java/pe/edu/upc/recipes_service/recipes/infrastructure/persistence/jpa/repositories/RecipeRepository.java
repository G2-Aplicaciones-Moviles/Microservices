package pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    boolean existsByName(String name);

    List<Recipe> findAllByCategoryId(Long categoryId);

    List<Recipe> findAllByAssignedToProfileId(int assignedToProfileId);
}
