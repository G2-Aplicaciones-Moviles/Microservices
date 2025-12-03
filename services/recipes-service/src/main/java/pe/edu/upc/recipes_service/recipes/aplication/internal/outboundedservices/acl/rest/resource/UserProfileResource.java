package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource;

import java.util.List;

public record UserProfileResource(
        int id,
        String gender,
        Double height,
        Double weight,
        int userScore,
        String birthDate,
        int activityLevelId,
        String activityLevelName,
        int objectiveId,
        String objectiveName,
        List<String> allergies
) {
}
