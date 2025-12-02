package pe.edu.upc.profiles_service.profiles.domain.services;

import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    List<Profile> getAllUsers();
    Optional<Profile> getUserById(Long id);
}
