package pe.edu.upc.recipes_service.recipes.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.UserId;
import pe.edu.upc.recipes_service.shared.domain.model.entities.AuditableModel;

@Getter
@Entity
@Table(name = "favorite_recipes")
@NoArgsConstructor
@ToString
public class FavoriteRecipe extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public FavoriteRecipe(UserId userId, Recipe recipe) {
        this.userId = userId;
        this.recipe = recipe;
    }

    public Long getUserId() {
        return this.userId.userId();
    }
}