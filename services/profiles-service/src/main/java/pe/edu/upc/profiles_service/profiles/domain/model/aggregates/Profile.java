package pe.edu.upc.profiles_service.profiles.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.profiles_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    @Getter
    @NotNull
    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @Getter
    @NotNull
    @Column(name = "email", length = 25, nullable = false)
    private String email;

    @Getter
    @NotNull
    @Column(name = "password", length = 25, nullable = false)
    private String password;

    @Getter
    @NotNull
    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Getter
    @NotNull
    @Column(name = "birthDate", length = 25, nullable = false)
    private String birthDate;

    @Getter
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userProfile_id", nullable = false)
    private UserProfile userProfile;

    public Profile(String name, String email, String password, Boolean isActive, String birthDate, UserProfile userProfile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.birthDate = birthDate;
        this.userProfile = userProfile;
    }
}
