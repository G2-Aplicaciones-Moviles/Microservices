package pe.edu.upc.profiles_service.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetAllUserProfilesQuery;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetUserProfileByIdQuery;
import pe.edu.upc.profiles_service.profiles.domain.services.UserProfileQueryService;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.UserProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileQueryServiceImpl implements UserProfileQueryService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileQueryServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserProfile> handle(GetAllUserProfilesQuery query) {
        return this.userProfileRepository.findAll();
    }

    @Override
    public Optional<UserProfile> handle(GetUserProfileByIdQuery query) {
        return this.userProfileRepository.findById(query.userProfileId());
    }
}
