package pe.edu.upc.profiles_service.profiles.domain.model.commands;

public record UpdateProfileCommand(
        String name,
        String email,

        String password,
        Boolean isActive,
        String birthDate,
        Long userProfileId
) {
}
