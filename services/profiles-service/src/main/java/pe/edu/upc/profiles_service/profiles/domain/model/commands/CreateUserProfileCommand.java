package pe.edu.upc.profiles_service.profiles.domain.model.commands;

import java.util.List;

public record CreateUserProfileCommand(
        Long userId,
        String gender,
        double height,
        double weight,
        int userScore,
        Long activityLevelId,
        Long objectiveId,
        List<Long> allergyIds,
        String birthDate
) {
}
