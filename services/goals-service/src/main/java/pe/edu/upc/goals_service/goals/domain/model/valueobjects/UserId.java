package pe.edu.upc.goals_service.goals.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class UserId {
    private Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("Invalid user id");
        this.value = value;
    }
}