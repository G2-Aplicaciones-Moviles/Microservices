package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource;

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

