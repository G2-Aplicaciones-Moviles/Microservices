package pe.edu.upc.profiles_service.profiles.domain.model.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.profiles_service.profiles.domain.model.valueobjects.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "allergies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "allergy_ingredients",
            joinColumns = @JoinColumn(name = "allergy_id")
    )
    @AttributeOverride(
            name = "name",
            column = @Column(name = "ingredient_name", nullable = false, length = 100)
    )
    private List<Ingredient> relatedIngredients = new ArrayList<>();

    /**
     * Agrega un ingrediente al Value Object
     */
    public void addIngredient(Ingredient ingredient) {
        this.relatedIngredients.add(ingredient);
    }

    /**
     * Elimina un ingrediente del Value Object
     */
    public void removeIngredient(Ingredient ingredient) {
        this.relatedIngredients.remove(ingredient);
    }
}
