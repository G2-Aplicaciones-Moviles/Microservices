package pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    /**
     * Devuelve el UserProfile con el ID más alto (el último creado).
     */
    Optional<UserProfile> findTopByOrderByIdDesc();

    // Buscar por userId (1:1)
    Optional<UserProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
