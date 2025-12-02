package pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.RecommendationStatus;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.TimeOfDay;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.UserId;
import pe.edu.upc.recommendations_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "recommendations")
public class Recommendation extends AuditableAbstractAggregateRoot<Recommendation> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = true))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    @JsonBackReference
    @NotNull(message = "El template no puede ser nulo")
    private RecommendationTemplate template;

    @Column(name = "reason", nullable = false)
    @NotBlank(message = "La razón no puede estar vacía")
    @Size(max = 255, message = "La razón no puede exceder los 255 caracteres")
    private String reason;

    @Column(name = "notes", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Las notas no pueden estar vacías")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_of_day", nullable = false)
    @NotNull(message = "El momento del día no puede ser nulo")
    private TimeOfDay timeOfDay;

    @Column(name = "score", nullable = false)
    @NotNull(message = "El puntaje no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El puntaje debe ser mayor o igual a 0")
    @DecimalMax(value = "10.0", inclusive = true, message = "El puntaje debe ser menor o igual a 10")
    private Double score;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "El estado no puede ser nulo")
    private RecommendationStatus status;

    @Column(name = "assigned_at", nullable = true)
    private LocalDateTime assignedAt;

    public Recommendation() {}

    public Recommendation(RecommendationTemplate template, String reason, String notes,
                          TimeOfDay timeOfDay, Double score, RecommendationStatus status) {
        this.userId = null; // Sin usuario asignado
        this.template = template;
        this.reason = reason;
        this.notes = notes;
        this.timeOfDay = timeOfDay;
        this.score = score;
        this.status = status;
        this.assignedAt = null; // Sin fecha de asignación
    }

    private Recommendation(UserId userId, RecommendationTemplate template, String reason, String notes,
                           TimeOfDay timeOfDay, Double score, RecommendationStatus status) {
        this.userId = userId;
        this.template = template;
        this.reason = reason;
        this.notes = notes;
        this.timeOfDay = timeOfDay;
        this.score = score;
        this.status = status;
        this.assignedAt = LocalDateTime.now();
    }

    public static Recommendation assignToUser(UserId userId, Long templateId, String reason, String notes,
                                              TimeOfDay timeOfDay, Double score, RecommendationStatus status) {
        RecommendationTemplate template = new RecommendationTemplate();
        template.setId(templateId);
        return new Recommendation(userId, template, reason, notes, timeOfDay, score, status);
    }
}