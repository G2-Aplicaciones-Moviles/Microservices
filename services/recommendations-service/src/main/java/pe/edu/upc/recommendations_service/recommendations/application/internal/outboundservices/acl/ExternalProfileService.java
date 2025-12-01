package pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.profiles.interfaces.acl.ProfileContextFacade;
import pe.edu.upc.center.jameoFit.profiles.interfaces.acl.UserProfilesContextFacade;

import java.util.Optional;

/**
 * External Profile Service
 * Esta clase actúa como ACL (Anti-Corruption Layer) entre los bounded contexts de Recommendation
 * para interactuar con los perfiles sin acoplamiento directo.
 */
@Service("recommendationsExternalProfileService")
public class ExternalProfileService {

    private final UserProfilesContextFacade userProfilesFacade;
    private final ProfileContextFacade profileContextFacade;

    public ExternalProfileService(UserProfilesContextFacade userProfilesFacade, ProfileContextFacade profileContextFacade) {
        this.userProfilesFacade = userProfilesFacade;
        this.profileContextFacade = profileContextFacade;
    }

    /**
     * Valida que un perfil exista antes de proceder con operaciones relacionadas a recomendaciones.
     * @param profileId ID del perfil (userId)
     * @throws IllegalArgumentException si el perfil no existe
     */
    public void validateProfileExists(Long profileId) {
        if (!existsProfile(profileId)) {
            throw new IllegalArgumentException("Profile not found with ID: " + profileId);
        }
    }

    /**
     * Verifica si un perfil existe por ID.
     */
    public boolean existsProfile(Long profileId) {
        return userProfilesFacade.existsProfileById(profileId);
    }

    /**
     * Obtiene el nombre del objetivo del perfil de usuario, si existe.
     */
    public Optional<String> getObjectiveNameByProfileId(Long profileId) {
        return userProfilesFacade.fetchObjectiveNameByProfileId(profileId);
    }

    /**
     * Obtiene el nombre del objetivo, validando antes que exista el perfil.
     * @throws IllegalArgumentException si no existe el perfil o no tiene objetivo definido
     */
    public String getValidatedObjectiveName(Long profileId) {
        validateProfileExists(profileId);
        return getObjectiveNameByProfileId(profileId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Profile exists but has no objective defined for ID: " + profileId));
    }
}