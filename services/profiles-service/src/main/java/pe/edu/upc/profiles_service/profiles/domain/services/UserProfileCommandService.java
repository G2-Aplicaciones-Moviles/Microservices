package pe.edu.upc.profiles_service.profiles.domain.services;

import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.DeleteUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.UpdateUserProfileCommand;

import java.util.Optional;


public interface UserProfileCommandService {
    int handle(CreateUserProfileCommand command);

    Optional<UserProfile> handle(UpdateUserProfileCommand command);

    void handle(DeleteUserProfileCommand command);
}