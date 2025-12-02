package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.profiles.interfaces.acl.UserProfilesContextFacade;
import pe.edu.upc.center.jameoFit.recipes.domain.model.valueobjects.MacronutrientValuesId;
import pe.edu.upc.center.jameoFit.recipes.domain.model.valueobjects.UserId;
import pe.edu.upc.center.jameoFit.tracking.interfaces.acl.TrackingContextFacade;

import java.util.Optional;

@Service
public class ExternalProfileAndTrackingService {

    private final UserProfilesContextFacade userProfilesContextFacade;
    private final TrackingContextFacade trackingContextFacade;

    public ExternalProfileAndTrackingService(UserProfilesContextFacade userProfilesContextFacade,
                                             @Lazy TrackingContextFacade trackingContextFacade) {

        this.userProfilesContextFacade = userProfilesContextFacade;
        this.trackingContextFacade = trackingContextFacade;
    }

    public boolean existsByUserId(UserId userId) {
        return userProfilesContextFacade.existsProfileById(userId.userId());
    }

    public Optional<String> getObjectiveNameByUserId(UserId userId) {
        return userProfilesContextFacade.fetchObjectiveNameByProfileId(userId.userId());
    }

    public void validateUserExists(UserId userId) {
        if (!existsByUserId(userId)) {
            throw new IllegalArgumentException("User profile not found with ID: " + userId.userId());
        }
    }

    public String getValidatedObjectiveName(UserId userId) {
        validateUserExists(userId);
        return getObjectiveNameByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User profile exists but has no objective defined for ID: " + userId.userId()));
    }

    public void validateMacronutrientValuesExists(MacronutrientValuesId macronutrientValuesId) {
        if (!trackingContextFacade.validateMacronutrientValuesExists(macronutrientValuesId.macronutrientValuesId())) {
            throw new IllegalArgumentException("MacronutrientValues not found with ID: " + macronutrientValuesId.macronutrientValuesId());
        }
    }
}