package pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long value) {
    public UserId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("User ID must be positive when provided.");
        }
    }

    public UserId() {
        this(null);
    }

    public Long getValue() {
        return value;
    }
}