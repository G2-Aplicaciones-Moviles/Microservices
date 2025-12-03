package pe.edu.upc.profiles_service.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class Ingredient {

    private String name;

    public Ingredient() {
        this.name = null;
    }

    public Ingredient(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ingredient name cannot be null or blank");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
