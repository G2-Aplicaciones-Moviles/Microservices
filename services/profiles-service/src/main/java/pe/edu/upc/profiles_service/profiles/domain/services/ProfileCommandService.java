package pe.edu.upc.profiles_service.profiles.domain.services;

import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.Profile;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateProfileCommand;

public interface ProfileCommandService {
    Profile handle(CreateProfileCommand command);
}
