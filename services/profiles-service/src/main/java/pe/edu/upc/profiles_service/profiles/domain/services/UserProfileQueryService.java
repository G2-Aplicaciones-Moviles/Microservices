package pe.edu.upc.profiles_service.profiles.domain.services;


import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetAllUserProfilesQuery;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetUserProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserProfileQueryService {
    Optional<UserProfile> handle(GetUserProfileByIdQuery query);
    List<UserProfile> handle(GetAllUserProfilesQuery query);
}
