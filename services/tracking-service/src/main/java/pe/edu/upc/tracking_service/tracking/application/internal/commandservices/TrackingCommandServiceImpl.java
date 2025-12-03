package pe.edu.upc.tracking_service.tracking.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.ExternalRecipeService;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.ExternalUserProfileService;
import pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource.RecipeNutritionResource;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MealPlanType;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;
import pe.edu.upc.tracking_service.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateMealPlanEntryToTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.RemoveMealPlanEntryFromTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.UpdateMealPlanEntryInTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingCommandService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrackingCommandServiceImpl implements TrackingCommandService {

    private static final Logger log = LoggerFactory.getLogger(TrackingCommandServiceImpl.class);

    private final TrackingRepository trackingRepository;
    private final TrackingMealPlanEntryRepository trackingMealPlanEntryRepository;
    private final TrackingGoalRepository trackingGoalRepository;
    private final MacronutrientValuesRepository macronutrientValuesRepository;
    private final TrackingMealPlanTypeRepository trackingMealPlanTypeRepository;
    private final ExternalUserProfileService externalUserProfileService;
    private final ExternalRecipeService externalRecipeService;

    public TrackingCommandServiceImpl(TrackingRepository trackingRepository, TrackingMealPlanEntryRepository mealPlanEntryRepository,
                                      TrackingGoalRepository trackingGoalRepository, MacronutrientValuesRepository macronutrientValuesRepository,
                                      TrackingMealPlanTypeRepository mealPlanTypeRepository, ExternalUserProfileService externalUserProfileService,
                                      @Lazy ExternalRecipeService externalRecipeService) {
        this.trackingRepository = trackingRepository;
        this.trackingMealPlanEntryRepository = mealPlanEntryRepository;
        this.trackingGoalRepository = trackingGoalRepository;
        this.macronutrientValuesRepository = macronutrientValuesRepository;
        this.trackingMealPlanTypeRepository = mealPlanTypeRepository;
        this.externalUserProfileService = externalUserProfileService;
        this.externalRecipeService = externalRecipeService;
    }

    /**
     * Helper: recalcula la suma de macros consumidos para un tracking,
     * persiste un nuevo MacronutrientValues, lo asocia al tracking y guarda.
     */
    private void recalculateAndPersistConsumedMacros(Tracking tracking) {
        long trackingIdLong;
        try {
            trackingIdLong = tracking.getId();
        } catch (Exception ex) {
            trackingIdLong = Long.parseLong(String.valueOf(tracking.getId()));
        }

        List<TrackingMealPlanEntry> entries = trackingMealPlanEntryRepository.findAllByTracking_Id(trackingIdLong);

        log.info("Recalculate macros for trackingId={} - found {} entries", trackingIdLong, entries.size());

        double totalCalories = 0.0;
        double totalCarbs = 0.0;
        double totalProteins = 0.0;
        double totalFats = 0.0;

        for (TrackingMealPlanEntry entry : entries) {
            try {
                Optional<RecipeNutritionResource> optNutrition = externalRecipeService.fetchNutritionByRecipeId(entry.getRecipeId());
                if (optNutrition.isEmpty()) {
                    // No romper: reportamos y seguimos con las demás entradas
                    log.warn("No nutrition info found for recipeId={} (trackingEntryId={}). Skipping entry.", entry.getRecipeId().recipeId(), entry.getId());
                    continue;
                }
                RecipeNutritionResource nutrition = optNutrition.get();
                log.debug("Entry id={} recipeId={} nutrition cal={} carb={} prot={} fat={}",
                        entry.getId(), entry.getRecipeId().recipeId(),
                        nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats());

                totalCalories += nutrition.calories();
                totalCarbs += nutrition.carbs();
                totalProteins += nutrition.proteins();
                totalFats += nutrition.fats();
            } catch (Exception e) {
                log.error("Error fetching nutrition for recipeId={} (entryId={}): {}", entry.getRecipeId() == null ? "null" : entry.getRecipeId().recipeId(), entry.getId(), e.getMessage(), e);
                // continuar con siguientes entradas
            }
        }

        // Crear y guardar macronutrient values resultante
        MacronutrientValues consumed = new MacronutrientValues(totalCalories, totalCarbs, totalProteins, totalFats);
        macronutrientValuesRepository.save(consumed);

        // Asociar al tracking y persistir
        tracking.setConsumedMacros(consumed);
        trackingRepository.save(tracking);

        log.info("Persisted consumed macros for trackingId={} -> calories={} carbs={} proteins={} fats={}",
                trackingIdLong, totalCalories, totalCarbs, totalProteins, totalFats);
    }

    @Override
    public Long handle(CreateMealPlanEntryToTrackingCommand command) {
        if (!externalRecipeService.existsByRecipeId(command.recipeId())) {
            throw new IllegalArgumentException("Recipe not found in Recipe bounded context with id: " + command.recipeId());
        }

        Optional<Tracking> trackingOpt = trackingRepository.findById(command.TrackingId());
        if (trackingOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking not found for id: " + command.TrackingId());
        }

        MealPlanType mealPlanType = trackingMealPlanTypeRepository.findByName(command.mealPlanType().getName())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de plan de comida inválido: " + command.mealPlanType()));

        Tracking tracking = trackingOpt.get();

        TrackingMealPlanEntry newEntry = new TrackingMealPlanEntry(
                command.recipeId(),
                mealPlanType,
                command.DayNumber()
        );
        newEntry.setTracking(tracking);

        var saved = trackingMealPlanEntryRepository.save(newEntry);
        log.info("Saved TrackingMealPlanEntry id={} trackingId={} recipeId={}", saved.getId(), tracking.getId(), saved.getRecipeId().recipeId());

        recalculateAndPersistConsumedMacros(tracking);

        return tracking.getId();
    }

    @Override
    public void handle(RemoveMealPlanEntryFromTrackingCommand command) {
        // Buscar el tracking por id
        Optional<Tracking> trackingOpt = trackingRepository.findById(command.TrackingId());
        if (trackingOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking not found with id: " + command.TrackingId());
        }

        Tracking tracking = trackingOpt.get();

        Optional<TrackingMealPlanEntry> mealPlanEntryOpt = trackingMealPlanEntryRepository.findById(command.MealPlanEntryId());
        if (mealPlanEntryOpt.isEmpty()) {
            throw new IllegalArgumentException("MealPlan entry not found with id: " + command.MealPlanEntryId());
        }

        TrackingMealPlanEntry mealPlanEntry = mealPlanEntryOpt.get();

        // Borrar el entry de la BD
        trackingMealPlanEntryRepository.deleteById(mealPlanEntry.getId());
        log.info("Deleted TrackingMealPlanEntry id={} from trackingId={}", mealPlanEntry.getId(), tracking.getId());

        // Recalcular macros y persistir
        recalculateAndPersistConsumedMacros(tracking);

        // Guardar el tracking por si aplica (timestamps, etc.)
        trackingRepository.save(tracking);
    }

    @Override
    public Optional<Tracking> handle(UpdateMealPlanEntryInTrackingCommand command) {
        if (!externalRecipeService.existsByRecipeId(command.recipeId())) {
            throw new IllegalArgumentException("Recipe not found in Recipe bounded context with id: " + command.recipeId());
        }

        Long trackingId = command.TrackingId();
        if (trackingId == 0L) {
            Optional<TrackingMealPlanEntry> mealPlanEntryOpt = trackingMealPlanEntryRepository.findById(command.MealPlanEntryId());
            if (mealPlanEntryOpt.isEmpty()) {
                throw new IllegalArgumentException("MealPlan not found with id: " + command.MealPlanEntryId());
            }
            TrackingMealPlanEntry existingEntry = mealPlanEntryOpt.get();
            trackingId = existingEntry.getTracking().getId();
        }

        Optional<Tracking> trackingOpt = trackingRepository.findById(trackingId);
        if (trackingOpt.isEmpty()) {
            return Optional.empty();
        }

        Tracking tracking = trackingOpt.get();

        Optional<TrackingMealPlanEntry> mealPlanEntryOpt = trackingMealPlanEntryRepository.findById(command.MealPlanEntryId());
        if (mealPlanEntryOpt.isEmpty()) {
            throw new IllegalArgumentException("MealPlan entry not found with id: " + command.MealPlanEntryId());
        }

        TrackingMealPlanEntry mealPlanEntry = mealPlanEntryOpt.get();

        MealPlanType mealPlanType = trackingMealPlanTypeRepository.findByName(command.mealPlanType())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de plan de comida inválido: " + command.mealPlanType()));

        // Actualizar fields del entry
        mealPlanEntry.setRecipeId(command.recipeId());
        mealPlanEntry.setMealPlanType(mealPlanType);
        mealPlanEntry.setDayNumber(command.dayNumber());

        // Guardar entry actualizado
        trackingMealPlanEntryRepository.save(mealPlanEntry);
        log.info("Updated TrackingMealPlanEntry id={} for trackingId={}", mealPlanEntry.getId(), tracking.getId());

        // Recalcular macros consumidos y persistir
        recalculateAndPersistConsumedMacros(tracking);

        // Devolver tracking actualizado
        Tracking savedTracking = trackingRepository.save(tracking);
        return Optional.of(savedTracking);
    }

    @Override
    public Long handle(CreateTrackingCommand command) {
        if (!externalUserProfileService.existsByUserId(command.profile())) {
            throw new IllegalArgumentException("User does not exist in Profile bounded context: " + command.profile().userId());
        }

        if (this.trackingRepository.existsByUserId(command.profile())) {
            throw new IllegalArgumentException("Tracking already exists for user: " + command.profile());
        }

        Optional<TrackingGoal> trackingGoalOpt = trackingGoalRepository.findByUserId(command.profile());
        if (trackingGoalOpt.isEmpty()) {
            throw new IllegalArgumentException("Tracking goal not found for user: " + command.profile());
        }

        TrackingGoal trackingGoal = trackingGoalOpt.get();
        LocalDate date = LocalDate.now();
        MacronutrientValues consumed = new MacronutrientValues(0, 0, 0, 0);
        macronutrientValuesRepository.save(consumed);

        var tracking = new Tracking(command.profile(), date, trackingGoal, consumed);

        try {
            trackingRepository.save(tracking);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving tracking:" + e.getMessage());
        }
        log.info("Created new tracking id={} for user={}", tracking.getId(), command.profile());
        return tracking.getId();
    }
}
