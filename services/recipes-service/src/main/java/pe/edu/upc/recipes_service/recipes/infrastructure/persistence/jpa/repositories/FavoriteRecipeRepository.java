package pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.FavoriteRecipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Long> {
    List<FavoriteRecipe> findByUserId(Long userId);
    Optional<FavoriteRecipe> findByUserIdAndRecipeId(Long userId, int recipeId);
    boolean existsByUserIdAndRecipeId(Long userId, int recipeId);
    void deleteByUserIdAndRecipeId(Long userId, int recipeId);
}