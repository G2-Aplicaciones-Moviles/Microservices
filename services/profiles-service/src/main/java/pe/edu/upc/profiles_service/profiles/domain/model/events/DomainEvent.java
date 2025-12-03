package pe.edu.upc.profiles_service.profiles.domain.model.events;

import lombok.Getter;

import java.time.Instant;

/**
 * Clase base para eventos de dominio dentro del bounded context de profiles.
 */
@Getter
public abstract class DomainEvent {
    private final String eventType;
    private final Instant occurredOn;

    protected DomainEvent(String eventType) {
        this.eventType = eventType;
        this.occurredOn = Instant.now();
    }

}
