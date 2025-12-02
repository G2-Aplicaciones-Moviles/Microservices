package pe.edu.upc.tracking_service.tracking.domain.model.valueobjects;


import jakarta.persistence.Embeddable;

@Embeddable
public record RecipeId(Long recipeId) {
    public RecipeId {
        if (recipeId < 0) {
            throw new IllegalArgumentException("Recipe Id must exists");
        }
    }

    public RecipeId() {
        this(0L);
    }
}