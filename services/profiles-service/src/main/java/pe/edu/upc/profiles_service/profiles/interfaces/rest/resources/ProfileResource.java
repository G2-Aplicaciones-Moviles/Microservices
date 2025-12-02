package pe.edu.upc.profiles_service.profiles.interfaces.rest.resources;

public record ProfileResource(
        int id,
        String name,
        String email,
        String password,
        Boolean isActive,
        String birthDate,
        int userProfileId
) {}
