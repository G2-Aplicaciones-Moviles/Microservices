package pe.edu.upc.profiles_service.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.Profile;
import pe.edu.upc.profiles_service.profiles.domain.services.ProfileQueryService;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profile> getAllUsers() {
        return profileRepository.findAll();
    }

    @Override
    public Optional<Profile> getUserById(Long id) {
        return profileRepository.findById(id);
    }
}
