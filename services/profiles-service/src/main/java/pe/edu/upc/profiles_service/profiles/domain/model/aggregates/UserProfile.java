package pe.edu.upc.profiles_service.profiles.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.ActivityLevel;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Allergy;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Objective;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateUserProfileCommand;
import pe.edu.upc.profiles_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate root: UserProfile
 * - Mantiene relaciones a ActivityLevel, Objective y Allergies (entities)
 * - Comportamientos de dominio (calculateCalorieNeeds, add/remove allergy, actualizar)
 * - Constructor desde CreateUserProfileCommand para respetar CQRS/DDD
 */
@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor
public class UserProfile extends AuditableAbstractAggregateRoot<UserProfile> {

    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotNull
    @Column(name = "gender", length = 25, nullable = false)
    private String gender;

    @NotNull
    @Column(name = "height", nullable = false)
    private double height;

    @NotNull
    @Column(name = "weight", nullable = false)
    private double weight;

    @NotNull
    @Column(name = "user_score", nullable = false)
    private int userScore;

    @NotNull
    @Column(name = "birth_date", length = 25, nullable = false)
    private String birthDate; // ✅ nuevo

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_level_id", nullable = false)
    private ActivityLevel activityLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "objective_id", nullable = false)
    private Objective objective;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_profile_allergies",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private List<Allergy> allergies = new ArrayList<>();

    /**
     * Constructor completo (útil para pruebas o creación interna).
     */
    public UserProfile(Long userId,
                       String gender,
                       double height,
                       double weight,
                       ActivityLevel activityLevel,
                       Objective objective,
                       int userScore,
                       String birthDate) {
        this.userId = Objects.requireNonNull(userId, "userId required");
        this.gender = Objects.requireNonNull(gender, "gender required");
        this.height = height;
        this.weight = weight;
        this.activityLevel = Objects.requireNonNull(activityLevel, "activityLevel required");
        this.objective = Objects.requireNonNull(objective, "objective required");
        this.userScore = userScore;
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate required");
        this.allergies = new ArrayList<>();
    }

    /**
     * Constructor desde CreateUserProfileCommand (puerta de entrada desde la capa de aplicación).
     * Aquí se cumple la responsabilidad: el aggregate sabe cómo inicializarse desde el comando.
     */
    public UserProfile(CreateUserProfileCommand command,
                       ActivityLevel activityLevel,
                       Objective objective) {
        Objects.requireNonNull(command, "command required");
        this.userId = Objects.requireNonNull(command.userId(), "userId required");
        this.gender = command.gender();
        this.height = command.height();
        this.weight = command.weight();
        this.userScore = command.userScore();
        this.birthDate = Objects.requireNonNull(command.birthDate(), "birthDate required");
        this.activityLevel = Objects.requireNonNull(activityLevel, "activityLevel required");
        this.objective = Objects.requireNonNull(objective, "objective required");
        this.allergies = new ArrayList<>();
    }

    /**
     * Actualiza campos importantes del perfil. Devuelve this para permitir encadenamiento si se desea.
     */
    public UserProfile updateProfile(String gender, double height, double weight,
                                     ActivityLevel activityLevel, Objective objective,
                                     int userScore, String birthDate) {
        this.gender = Objects.requireNonNull(gender, "gender required");
        this.height = height;
        this.weight = weight;
        this.activityLevel = Objects.requireNonNull(activityLevel, "activityLevel required");
        this.objective = Objects.requireNonNull(objective, "objective required");
        this.userScore = userScore;
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate required");
        return this;
    }

    public void addAllergy(Allergy allergy) {
        if (allergy == null) return;
        if (!this.allergies.contains(allergy)) this.allergies.add(allergy);
    }

    public void removeAllergy(Allergy allergy) {
        if (allergy == null) return;
        this.allergies.remove(allergy);
    }

    public double calculateCalorieNeeds(int age) {
        return this.activityLevel.calculateCalories(this.weight, this.height, age);
    }

    public void updateActivityLevel(ActivityLevel newLevel) {
        this.activityLevel = Objects.requireNonNull(newLevel, "newLevel required");
    }

    public void updateObjective(Objective newObjective) {
        this.objective = Objects.requireNonNull(newObjective, "newObjective required");
    }
}
