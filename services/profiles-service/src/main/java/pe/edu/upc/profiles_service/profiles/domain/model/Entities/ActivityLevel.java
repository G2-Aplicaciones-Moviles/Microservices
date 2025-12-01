package pe.edu.upc.profiles_service.profiles.domain.model.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.profiles_service.shared.domain.model.entities.AuditableModel;

@Getter
@Setter
@Entity
@Table(name = "activity_levels")
public class ActivityLevel extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @NotNull
    @Column(name = "activity_factor", nullable = false)
    private double activityFactor;

    public ActivityLevel() {
    }

    public ActivityLevel(String name, String description, double activityFactor) {
        this.name = name;
        this.description = description;
        this.activityFactor = activityFactor;
    }

    public double calculateCalories(double weight, double height, int age) {
        double bmr = 10 * weight + 6.25 * height - 5 * age + 5; // Suponiendo hombre
        return bmr * this.activityFactor;
    }

    public String describeLevel() {
        return this.name + ": " + this.description;
    }

    public void updateLevel(String name, String description, double activityFactor) {
        this.name = name;
        this.description = description;
        this.activityFactor = activityFactor;
    }
}
