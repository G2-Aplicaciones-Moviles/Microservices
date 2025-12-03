package pe.edu.upc.recipes_service.recipes.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name = "recipe_types")
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class RecipeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    public RecipeType(String name) {
        this.name = name;
    }
}
