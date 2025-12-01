package pe.edu.upc.recipes_service.recipes.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Ingredient;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;

@Getter
@Setter
@Entity
@Table(
        name = "recipe_ingredients",
        uniqueConstraints = @UniqueConstraint(name="uk_recipe_ingredient", columnNames={"recipe_id","ingredient_id"})
)
@NoArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    // cantidad en gramos (ajústalo si usarás otra unidad)
    @Column(name = "amount_grams", nullable = false)
    private double amountGrams;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, double amountGrams) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amountGrams = amountGrams;
    }
}
