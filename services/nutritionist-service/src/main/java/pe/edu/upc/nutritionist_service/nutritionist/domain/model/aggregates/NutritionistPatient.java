package pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.ServiceType;
import pe.edu.upc.nutritionist_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nutritionist_patients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_nutritionist_patient", columnNames = {"nutritionist_id", "patient_user_id"})
        })
public class NutritionistPatient extends AuditableAbstractAggregateRoot<NutritionistPatient> {

    @NotNull
    @Column(name = "nutritionist_id", nullable = false)
    private Integer nutritionistId;

    @NotNull
    @Column(name = "patient_user_id", nullable = false)
    private Long patientUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    // Solo para DIET_PLAN
    @Column(name = "start_date")
    private Date startDate;

    // Solo para PERSONAL_CONSULT
    @Column(name = "scheduled_at")
    private Date scheduledAt;

    @NotNull
    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "requested_at", nullable = false)
    private Date requestedAt;

    public NutritionistPatient(Integer nutritionistId,
                               Long patientUserId,
                               ServiceType serviceType,
                               Date startDate,
                               Date scheduledAt) {

        this.nutritionistId = nutritionistId;
        this.patientUserId = patientUserId;
        this.serviceType = serviceType;
        this.startDate = startDate;
        this.scheduledAt = scheduledAt;
        this.accepted = false;
        this.requestedAt = new Date();
    }

    public void approve() {
        this.accepted = true;
    }

    public void reject() {
        this.accepted = false;
    }
}
