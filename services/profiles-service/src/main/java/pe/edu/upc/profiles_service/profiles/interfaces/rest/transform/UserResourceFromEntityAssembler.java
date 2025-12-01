package pe.edu.upc.profiles_service.profiles.interfaces.rest.transform;

import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.Profile;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.ProfileResource;

import java.util.List;
import java.util.stream.Collectors;

public class UserResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile profile) {
        return new ProfileResource(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getPassword(),
                profile.getIsActive(),
                profile.getBirthDate(),
                profile.getUserProfile().getId()
        );
    }

    public static List<ProfileResource> toResources(List<Profile> profiles) {
        return profiles.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
    }
}
