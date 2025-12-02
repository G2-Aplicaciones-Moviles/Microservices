package pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.Specialty;
import pe.edu.upc.nutritionist_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nutritionists",
        uniqueConstraints = { @UniqueConstraint(name = "uk_nutritionist_user", columnNames = "user_id") })
public class Nutritionist extends AuditableAbstractAggregateRoot<Nutritionist> {

    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotNull
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotNull
    @Column(name = "license_number", nullable = false, length = 50)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", nullable = false)
    private Specialty specialty;

    @NotNull
    @Column(name = "years_experience", nullable = false)
    private Integer yearsExperience;

    @Column(name = "accepting_new_patients")
    private Boolean acceptingNewPatients;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public Nutritionist(Long userId, String fullName, String licenseNumber,
                        Specialty specialty, Integer yearsExperience,
                        Boolean acceptingNewPatients, String bio, String profilePictureUrl) {
        this.userId = userId;
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.specialty = specialty;
        this.yearsExperience = yearsExperience;
        this.acceptingNewPatients = acceptingNewPatients;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.updatedAt = new Date();
    }

    public void updateInfo(String fullName, String bio, String profilePictureUrl,
                           Boolean acceptingNewPatients, Integer yearsExperience) {
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.acceptingNewPatients = acceptingNewPatients;
        this.yearsExperience = yearsExperience;
        this.updatedAt = new Date();
    }
}
