package pe.edu.upc.tracking_service.tracking.domain.model.dto;

/**
 * DTO/Record que el bounded context Tracking consume desde el ACL.
 * Contiene solo los datos que Tracking necesita para calcular calorías/macros.
 */
public record UserProfileDto(
        Long userProfileId,
        String gender,
        double heightMeters,
        double weightKg,
        double activityFactor,
        String objectiveName,
        String birthDate
) {}
