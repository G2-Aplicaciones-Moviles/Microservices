package pe.edu.upc.recipes_service.recipes.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.Category;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeIngredient;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "recipes")
@ToString
public class Recipe extends AuditableAbstractAggregateRoot<Recipe> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "preparation_time")
    private int preparationTime;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "created_by_nutritionist_id", nullable = true)
    private Long createdByNutritionistId;

    @Column(name = "assigned_to_profile_id", nullable = true)
    private Integer assignedToProfileId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "recipe_type_id", nullable = false)
    private RecipeType recipeType;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public Recipe() {
    }

    public Recipe(String name, String description, int preparationTime,
                  String difficulty, Category category, RecipeType recipeType,
                  Long createdByNutritionistId, Integer assignedToProfileId) { // Acepta los nuevos IDs

        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
        this.category = category;
        this.recipeType = recipeType;
        this.createdByNutritionistId = createdByNutritionistId;
        this.assignedToProfileId = assignedToProfileId;
    }

    public void addIngredient(Ingredient ingredient, double amountGrams) {
        var exists = this.recipeIngredients.stream()
                .anyMatch(ri -> ri.getIngredient().getId() == ingredient.getId());

        if (exists) throw new IllegalArgumentException("Ingredient already added to the recipe.");

        var ri = new RecipeIngredient(this, ingredient, amountGrams);
        this.recipeIngredients.add(ri);
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

}