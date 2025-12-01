package pe.edu.upc.profiles_service.profiles.interfaces.rest.resources;

import java.util.List;

public record CreateUserProfileResource(
        Long userId,            // <-- nuevo
        String gender,
        double height,
        double weight,
        int userScore,
        Long activityLevelId,
        Long objectiveId,
        List<Long> allergyIds,
        String birthDate // ✅ nuevo

) {}
