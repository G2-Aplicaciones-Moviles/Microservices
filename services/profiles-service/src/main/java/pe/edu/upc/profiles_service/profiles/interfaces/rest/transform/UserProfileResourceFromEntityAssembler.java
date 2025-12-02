package pe.edu.upc.profiles_service.profiles.interfaces.rest.transform;

import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.UserProfileResource;

import java.util.List;
import java.util.stream.Collectors;

public class UserProfileResourceFromEntityAssembler {

    public static UserProfileResource toResourceFromEntity(UserProfile entity) {
        return new UserProfileResource(
                entity.getId().intValue(),
                entity.getGender(),
                entity.getHeight(),
                entity.getWeight(),
                entity.getUserScore(),
                entity.getBirthDate(),  // ✅ String
                entity.getActivityLevel().getId(),
                entity.getActivityLevel().getName(),
                entity.getObjective().getId(),
                entity.getObjective().getObjectiveName(),
                entity.getAllergies().stream()
                        .map(a -> a.getName())
                        .collect(Collectors.toList())
        );
    }

    public static List<UserProfileResource> toResources(List<UserProfile> entities) {
        return entities.stream()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
