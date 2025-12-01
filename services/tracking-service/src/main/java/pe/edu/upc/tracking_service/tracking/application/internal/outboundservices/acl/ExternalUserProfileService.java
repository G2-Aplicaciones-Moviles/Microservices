package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.tracking_service.profiles.interfaces.acl.ProfileContextFacade;
import pe.edu.upc.tracking_service.profiles.interfaces.acl.UserProfilesContextFacade;
import pe.edu.upc.tracking_service.tracking.domain.model.dto.UserProfileDto;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

import java.util.Objects;
import java.util.Optional;

/**
 * ACL entre Tracking y Profiles (mapea UserProfile -> UserProfileDto).
 */
@Service
public class ExternalUserProfileService {

    private final UserProfilesContextFacade userProfilesFacade;
    private final ProfileContextFacade profileContextFacade;

    public ExternalUserProfileService(UserProfilesContextFacade userProfilesFacade,
                                      ProfileContextFacade profileContextFacade) {
        this.userProfilesFacade = userProfilesFacade;
        this.profileContextFacade = profileContextFacade;
    }

    /**
     * Verifica si existe un perfil asociado a un UserId.
     */
    public boolean existsByUserId(UserId userId) {
        if (userId == null || userId.userId() == null) return false;

        return userProfilesFacade.fetchAll().stream()
                .anyMatch(profileResource -> Objects.equals(Long.valueOf(profileResource.id()), userId.userId()));
    }

    /**
     * Obtiene el nombre del objetivo (goal) de un perfil de usuario.
     */
    public Optional<String> getObjectiveNameByProfileId(Long profileId) {
        return userProfilesFacade.fetchObjectiveNameByProfileId(profileId);
    }

    /**
     * Verifica si un perfil existe.
     */
    public boolean existsProfile(Long profileId) {
        return userProfilesFacade.existsProfileById(profileId);
    }

    /**
     * Lanza excepción si el perfil no existe.
     */
    public void validateProfileExists(Long profileId) {
        if (!existsProfile(profileId)) {
            throw new IllegalArgumentException("Profile not found with ID: " + profileId);
        }
    }

    /**
     * Obtiene el nombre del objetivo validando existencia.
     */
    public String getValidatedObjectiveName(Long profileId) {
        validateProfileExists(profileId);

        return getObjectiveNameByProfileId(profileId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Profile exists but has no objective defined for ID: " + profileId));
    }

    /**
     * Devuelve un UserProfileDto construido desde el aggregate UserProfile.
     *
     * UserProfileDto(
     *   Long userProfileId,
     *   String gender,
     *   double heightMeters,
     *   double weightKg,
     *   double activityFactor,
     *   String objectiveName,
     *   String birthDate
     * )
     */
    public Optional<UserProfileDto> fetchUserProfileDtoById(Long profileId) {
        if (profileId == null) return Optional.empty();

        return userProfilesFacade.fetchUserProfileById(profileId)
                .map(this::mapToDto);
    }

    private UserProfileDto mapToDto(UserProfile entity) {
        if (entity == null) return null;

        // userProfileId: adaptar posible tipo de getId() (int/Integer/Long)
        Long userProfileId = null;
        try {
            Object rawId = entity.getId();
            if (rawId instanceof Long) userProfileId = (Long) rawId;
            else if (rawId instanceof Integer) userProfileId = Long.valueOf((Integer) rawId);
            else if (rawId != null) userProfileId = Long.valueOf(rawId.toString());
        } catch (Exception ignored) { }

        String gender = null;
        try { gender = entity.getGender(); } catch (Exception ignored) { }

        double heightMeters = 0.0;
        try { heightMeters = entity.getHeight(); } catch (Exception ignored) { }

        double weightKg = 0.0;
        try { weightKg = entity.getWeight(); } catch (Exception ignored) { }

        double activityFactor = 1.0;
        try {
            if (entity.getActivityLevel() != null) {
                activityFactor = entity.getActivityLevel().getActivityFactor();
            }
        } catch (Exception ignored) { }

        String objectiveName = null;
        try {
            if (entity.getObjective() != null) {
                objectiveName = entity.getObjective().getObjectiveName();
            }
        } catch (Exception ignored) { }

        String birthDate = null;
        try { birthDate = entity.getBirthDate(); } catch (Exception ignored) { }

        return new UserProfileDto(
                userProfileId,
                gender,
                heightMeters,
                weightKg,
                activityFactor,
                objectiveName,
                birthDate
        );
    }
}
