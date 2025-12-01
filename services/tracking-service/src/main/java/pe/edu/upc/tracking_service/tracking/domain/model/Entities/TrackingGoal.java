package pe.edu.upc.tracking_service.tracking.domain.model.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.tracking_service.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

@Getter
@Entity
@Table(name = "tracking_goal")
public class TrackingGoal extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @NotNull
    @Embedded
    private UserId userId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "target_macros")
    @Setter
    private MacronutrientValues targetMacros;


    public TrackingGoal() {
    }

    public TrackingGoal(UserId userId, MacronutrientValues targetMacros) {
        this.userId = userId;
        this.targetMacros = targetMacros;
    }

    // Método para actualizar los macros del objetivo
    public void updateTargetMacros(MacronutrientValues newTargetMacros) {
        this.targetMacros = newTargetMacros;
    }
}
