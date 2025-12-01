package pe.edu.upc.tracking_service.tracking.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;
import pe.edu.upc.tracking_service.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.*;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.*;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.domain.services.*;

import java.util.List;
import java.util.Optional;

@Service
public class TrackingContextFacade {

    private final TrackingCommandService trackingCommandService;
    private final TrackingQueryService trackingQueryService;
    private final TrackingGoalCommandService trackingGoalCommandService;
    private final TrackingGoalQueryService trackingGoalQueryService;
    private final MacronutrientValuesCommandService macronutrientValuesCommandService;
    private final MacronutrientValuesQueryService macronutrientValuesQueryService; // Agregar este

    public TrackingContextFacade(TrackingCommandService trackingCommandService,
                                 TrackingQueryService trackingQueryService,
                                 TrackingGoalCommandService trackingGoalCommandService,
                                 TrackingGoalQueryService trackingGoalQueryService,
                                 MacronutrientValuesCommandService macronutrientValuesCommandService,
                                 MacronutrientValuesQueryService macronutrientValuesQueryService) { // Agregar este
        this.trackingCommandService = trackingCommandService;
        this.trackingQueryService = trackingQueryService;
        this.trackingGoalCommandService = trackingGoalCommandService;
        this.trackingGoalQueryService = trackingGoalQueryService;
        this.macronutrientValuesCommandService = macronutrientValuesCommandService;
        this.macronutrientValuesQueryService = macronutrientValuesQueryService; // Agregar este
    }

    // === TRACKING OPERATIONS ===

    /**
     * Crea un nuevo tracking para un usuario
     */
    public Long createTracking(CreateTrackingCommand command) {
        return (long) trackingCommandService.handle(command);
    }

    /**
     * Obtiene el tracking de un usuario por su ID
     */
    public Optional<Tracking> getTrackingByUserId(Long userId) {
        return trackingQueryService.handle(new GetTrackingByUserIdQuery(new UserId(userId)));
    }

    /**
     * Obtiene los macronutrientes consumidos de un tracking
     */
    public Optional<MacronutrientValues> getConsumedMacros(Long trackingId) {
        return trackingQueryService.handle(new GetConsumedMacrosQuery(trackingId));
    }

    // === MEAL PLAN ENTRY OPERATIONS ===

    /**
     * Agrega una nueva entrada de meal plan a un tracking
     */
    public Long addMealPlanEntryToTracking(CreateMealPlanEntryToTrackingCommand command) {
        return (long) trackingCommandService.handle(command);
    }

    /**
     * Remueve una entrada de meal plan de un tracking
     */
    public void removeMealPlanEntryFromTracking(RemoveMealPlanEntryFromTrackingCommand command) {
        trackingCommandService.handle(command);
    }

    /**
     * Actualiza una entrada de meal plan en un tracking
     */
    public Optional<Tracking> updateMealPlanEntryInTracking(UpdateMealPlanEntryInTrackingCommand command) {
        return trackingCommandService.handle(command);
    }

    /**
     * Obtiene todas las comidas de un tracking
     */
    public List<TrackingMealPlanEntry> getAllMealsByTrackingId(Long trackingId) {
        return trackingQueryService.handle(new GetAllMealsQuery(trackingId));
    }

    // === TRACKING GOAL OPERATIONS ===

    /**
     * Crea un nuevo tracking goal para un usuario
     */
    public Long createTrackingGoal(CreateTrackingGoalCommand command) {
        return trackingGoalCommandService.handle(command);
    }

    /**
     * Obtiene el tracking goal de un usuario por su ID
     */
    public Optional<TrackingGoal> getTrackingGoalByUserId(Long userId) {
        return trackingGoalQueryService.handle(new GetTrackingGoalByUserIdQuery(new UserId(userId)));
    }

    /**
     * Obtiene los macronutrientes objetivo de un tracking goal
     */
    public Optional<MacronutrientValues> getTargetMacronutrients(Long trackingGoalId) {
        return trackingGoalQueryService.handle(new GetTargetMacronutrientsQuery(trackingGoalId));
    }

    // === MACRONUTRIENT VALUES OPERATIONS ===

    /**
     * Crea nuevos valores de macronutrientes
     */
    public Long createMacronutrientValues(CreateMacronutrientValuesCommand command) {
        return macronutrientValuesCommandService.handle(command);
    }

    /**
     * Obtiene macronutrientes por ID
     */
    public Optional<MacronutrientValues> getMacronutrientValuesById(Long macronutrientValuesId) {
        return macronutrientValuesQueryService.handle(new GetMacronutrientValuesByIdQuery(macronutrientValuesId));
    }

    /**
     * Valida si existen macronutrientes con el ID especificado
     */
    public boolean validateMacronutrientValuesExists(Long macronutrientValuesId) {
        return getMacronutrientValuesById(macronutrientValuesId).isPresent();
    }

    // === BUSINESS OPERATIONS ===

    /**
     * Operación completa: Crea un tracking goal y después un tracking para el usuario
     */
    public Long createCompleteTrackingSetup(Long userId, MacronutrientValues targetMacros) {
        // Primero crear el tracking goal
        CreateTrackingGoalCommand goalCommand = new CreateTrackingGoalCommand((new UserId(userId)), targetMacros);
        Long goalId = createTrackingGoal(goalCommand);

        // Después crear el tracking
        CreateTrackingCommand trackingCommand = new CreateTrackingCommand(new UserId(userId));
        return createTracking(trackingCommand);
    }
}