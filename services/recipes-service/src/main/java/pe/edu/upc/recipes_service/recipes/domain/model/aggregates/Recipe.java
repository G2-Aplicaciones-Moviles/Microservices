package pe.edu.upc.recipes_service.recipes.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.Category;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeIngredient;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.UserId;
import pe.edu.upc.recipes_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "recipes")
@ToString
public class Recipe extends AuditableAbstractAggregateRoot<Recipe> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    })
    private UserId userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "preparation_time")
    private int preparationTime;

    @Column(name = "difficulty")
    private String difficulty;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "recipe_type_id", nullable = false)
    private RecipeType recipeType;

    // ⬇️ ÚNICA colección válida tras la migración a entidad puente
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void addIngredient(Ingredient ingredient, double amountGrams) {
        var exists = this.recipeIngredients.stream()
                .anyMatch(ri -> ri.getIngredient().getId() == ingredient.getId()); // ✅

        if (exists) throw new IllegalArgumentException("Ingredient already added to the recipe.");

        var ri = new RecipeIngredient(this, ingredient, amountGrams);
        this.recipeIngredients.add(ri);
    }

    public Recipe() { this.userId = new UserId(); }

    public Recipe(Long userId, String name, String description, int preparationTime,
                  String difficulty, Category category, RecipeType recipeType) {
        this.userId = new UserId(userId);
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
        this.category = category;
        this.recipeType = recipeType;
    }

    public void updateRecipe(String name, String description, int preparationTime, String difficulty,
                             Category category, RecipeType recipeType) {
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
        this.category = category;
        this.recipeType = recipeType;
    }

    public Long getUserId() { return this.userId.userId(); }
}
