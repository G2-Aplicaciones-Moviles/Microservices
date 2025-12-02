package pe.edu.upc.tracking_service.tracking.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.ExternalUserProfileService;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.*;
import pe.edu.upc.tracking_service.tracking.domain.model.dto.UserProfileDto;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.domain.services.CalorieCalculatorService;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingGoalCommandService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.MacronutrientValuesRepository;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.TrackingGoalRepository;

import java.util.Optional;

@Service
@Transactional
public class TrackingGoalCommandServiceImpl implements TrackingGoalCommandService {

    private final TrackingGoalRepository trackingGoalRepository;
    private final MacronutrientValuesRepository macronutrientValuesRepository;
    private final ExternalUserProfileService externalUserProfileService;

    public TrackingGoalCommandServiceImpl(TrackingGoalRepository trackingGoalRepository,
                                          MacronutrientValuesRepository macronutrientValuesRepository,
                                          ExternalUserProfileService externalUserProfileService) {
        this.trackingGoalRepository = trackingGoalRepository;
        this.macronutrientValuesRepository = macronutrientValuesRepository;
        this.externalUserProfileService = externalUserProfileService;
    }

    @Override
    public Long handle(CreateTrackingGoalCommand command) {
        // Validar que el perfil existe
        externalUserProfileService.validateProfileExists(command.profile().userId());

        if (trackingGoalRepository.existsByUserId(command.profile())) {
            throw new IllegalArgumentException("Tracking goal already exists for user: " + command.profile());
        }

        var trackingGoal = new TrackingGoal(command.profile(), command.macronutrientValues());
        trackingGoalRepository.save(trackingGoal);
        return trackingGoal.getId();
    }

    @Override
    public void handle(UpdateTrackingGoalCommand command) {
        // Validar que el perfil existe
        externalUserProfileService.validateProfileExists(command.userId().userId());

        Optional<TrackingGoal> trackingGoalOpt = trackingGoalRepository.findByUserId(command.userId());

        if (trackingGoalOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking goal not found for user: " + command.userId());
        }

        TrackingGoal trackingGoal = trackingGoalOpt.get();

        // Crear nuevos valores de macronutrientes basados en el tipo de objetivo (legacy)
        MacronutrientValues newMacros = new MacronutrientValues(
                command.goalType().getCalories(),
                command.goalType().getCarbs(),
                command.goalType().getProteins(),
                command.goalType().getFats()
        );

        // Guardar los nuevos valores de macronutrientes
        macronutrientValuesRepository.save(newMacros);

        // Actualizar el tracking goal con los nuevos macros
        TrackingGoal updatedTrackingGoal = new TrackingGoal(command.userId(), newMacros);
        updatedTrackingGoal.setId(trackingGoal.getId()); // Mantener el mismo ID

        trackingGoalRepository.save(updatedTrackingGoal);
    }

    /**
     * Crea un tracking goal automáticamente basado en el objetivo del perfil
     *
     * @param profileId ID del perfil
     * @return ID del tracking goal creado
     */
    public Long createTrackingGoalFromProfile(Long profileId) {
        Optional<UserProfileDto> profileDtoOpt = externalUserProfileService.getUserProfileDtoById(profileId);
        UserProfileDto profileDto = profileDtoOpt.orElseThrow(() ->
                new IllegalArgumentException("UserProfile not found for id: " + profileId));

        MacronutrientValues macros = CalorieCalculatorService.calculateTargetMacronutrients(profileDto);

        macronutrientValuesRepository.save(macros);

        var command = new CreateTrackingGoalCommand(
                new UserId(profileId),
                macros
        );

        return handle(command);
    }

    /**
     * Actualiza un tracking goal basado en el objetivo actual del perfil
     *
     * @param profileId ID del perfil
     */
    public void updateTrackingGoalFromProfile(Long profileId) {
        Optional<UserProfileDto> profileDtoOpt = externalUserProfileService.getUserProfileDtoById(profileId);
        UserProfileDto profileDto = profileDtoOpt.orElseThrow(() ->
                new IllegalArgumentException("UserProfile not found for id: " + profileId));

        MacronutrientValues macros = CalorieCalculatorService.calculateTargetMacronutrients(profileDto);

        macronutrientValuesRepository.save(macros);

        Optional<TrackingGoal> trackingGoalOpt = trackingGoalRepository.findByUserId(new UserId(profileId));
        if (trackingGoalOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking goal not found for user: " + profileId);
        }
        TrackingGoal trackingGoal = trackingGoalOpt.get();

        try {
            trackingGoal.updateTargetMacros(macros);
        } catch (NoSuchMethodError e) {
            try {
                trackingGoal.setTargetMacros(macros);
            } catch (Exception ignored) {
            }
        }

        trackingGoalRepository.save(trackingGoal);
    }

    /**
     * Verifica si un tracking goal existe para un perfil
     *
     * @param profileId ID del perfil
     * @return true si existe, false en caso contrario
     */
    public boolean existsTrackingGoalForProfile(Long profileId) {
        return trackingGoalRepository.existsByUserId(
                new UserId(profileId)
        );
    }
}
