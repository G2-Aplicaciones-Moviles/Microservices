package pe.edu.upc.tracking_service.tracking.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.ExternalProfileService;
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
    private final ExternalProfileService externalProfileService;

    public TrackingGoalCommandServiceImpl(TrackingGoalRepository trackingGoalRepository,
                                          MacronutrientValuesRepository macronutrientValuesRepository,
                                          ExternalUserProfileService externalUserProfileService,
                                          ExternalProfileService externalProfileService) {
        this.trackingGoalRepository = trackingGoalRepository;
        this.macronutrientValuesRepository = macronutrientValuesRepository;
        this.externalUserProfileService = externalUserProfileService;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public Long handle(CreateTrackingGoalCommand command) {
        externalUserProfileService.validateUserExists(command.profile());

        if (trackingGoalRepository.existsByUserId(command.profile())) {
            throw new IllegalArgumentException("Tracking goal already exists for user: " + command.profile());
        }

        var trackingGoal = new TrackingGoal(command.profile(), command.macronutrientValues());
        trackingGoalRepository.save(trackingGoal);
        return trackingGoal.getId();
    }

    @Override
    public void handle(UpdateTrackingGoalCommand command) {
        externalUserProfileService.validateUserExists(command.userId());

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
     * Creates a tracking goal automatically based on the user's profile objective.
     * Fetches detailed profile information from profiles-service and calculates macros.
     *
     * @param userId ID of the user
     * @return ID of the created tracking goal
     */
    public Long createTrackingGoalFromProfile(Long userId) {
        try {
            System.out.println(">>> [TrackingGoal] Starting creation for userId: " + userId);

            // Validación 1: Usuario existe en IAM
            System.out.println(">>> [TrackingGoal] Validating user exists in IAM...");
            externalUserProfileService.validateUserExists(userId);
            System.out.println(">>> [TrackingGoal] User exists in IAM ✓");

            // Validación 2: Perfil existe en Profiles
            System.out.println(">>> [TrackingGoal] Validating profile exists in Profiles...");
            externalProfileService.validateProfileExists(userId);
            System.out.println(">>> [TrackingGoal] Profile exists in Profiles ✓");

            // Obtener datos del perfil
            System.out.println(">>> [TrackingGoal] Fetching profile data...");
            Optional<UserProfileDto> profileDtoOpt = externalProfileService.getUserProfileDtoByUserId(userId);
            UserProfileDto profileDto = profileDtoOpt.orElseThrow(() -> {
                System.err.println(">>> [TrackingGoal] ERROR: UserProfile not found for userId: " + userId);
                return new IllegalArgumentException("UserProfile not found for userId: " + userId);
            });
            System.out.println(">>> [TrackingGoal] Profile data fetched ✓ (height=" + profileDto.heightMeters() +
                             ", weight=" + profileDto.weightKg() + ", objective=" + profileDto.objectiveName() + ")");

            // Calcular macros
            System.out.println(">>> [TrackingGoal] Calculating macronutrients...");
            MacronutrientValues macros = CalorieCalculatorService.calculateTargetMacronutrients(profileDto);
            System.out.println(">>> [TrackingGoal] Macros calculated ✓ (calories=" + macros.getCalories() +
                             ", carbs=" + macros.getCarbs() + ", proteins=" + macros.getProteins() +
                             ", fats=" + macros.getFats() + ")");

            macronutrientValuesRepository.save(macros);
            System.out.println(">>> [TrackingGoal] Macros saved to DB ✓");

            // Crear comando y ejecutar
            System.out.println(">>> [TrackingGoal] Creating tracking goal...");
            var command = new CreateTrackingGoalCommand(new UserId(userId), macros);
            Long goalId = handle(command);
            System.out.println(">>> [TrackingGoal] Tracking goal created successfully ✓ (goalId=" + goalId + ")");

            return goalId;
        } catch (IllegalArgumentException e) {
            System.err.println(">>> [TrackingGoal] VALIDATION ERROR for userId " + userId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(">>> [TrackingGoal] UNEXPECTED ERROR for userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to create tracking goal: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a tracking goal based on the current profile objective.
     * Fetches latest profile information from profiles-service and recalculates macros.
     *
     * @param userId ID of the user
     */
    public void updateTrackingGoalFromProfile(Long userId) {
        externalUserProfileService.validateUserExists(userId);
        externalProfileService.validateProfileExists(userId);

        Optional<UserProfileDto> profileDtoOpt = externalProfileService.getUserProfileDtoByUserId(userId);
        UserProfileDto profileDto = profileDtoOpt.orElseThrow(() ->
                new IllegalArgumentException("UserProfile not found for userId: " + userId));

        MacronutrientValues macros = CalorieCalculatorService.calculateTargetMacronutrients(profileDto);
        macronutrientValuesRepository.save(macros);

        Optional<TrackingGoal> trackingGoalOpt = trackingGoalRepository.findByUserId(new UserId(userId));
        if (trackingGoalOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking goal not found for user: " + userId);
        }
        TrackingGoal trackingGoal = trackingGoalOpt.get();

        trackingGoal.updateTargetMacros(macros);
        trackingGoalRepository.save(trackingGoal);
    }

    /**
     * Checks if a tracking goal exists for a user.
     *
     * @param userId ID of the user
     * @return true if exists, false otherwise
     */
    public boolean existsTrackingGoalForProfile(Long userId) {
        return trackingGoalRepository.existsByUserId(new UserId(userId));
    }
}
