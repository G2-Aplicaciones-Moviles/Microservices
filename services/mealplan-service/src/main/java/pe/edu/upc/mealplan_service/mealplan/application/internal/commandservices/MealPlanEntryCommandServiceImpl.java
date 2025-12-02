package pe.edu.upc.mealplan_service.mealplan.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.ExternalRecipeService;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.ExternalTrackingService;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanEntryCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanTypes;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.RecipeId;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanEntryCommandService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanEntryRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanTypeRepository;

@Service
public class MealPlanEntryCommandServiceImpl implements MealPlanEntryCommandService {

    private static final Logger logger = LoggerFactory.getLogger(MealPlanEntryCommandServiceImpl.class);

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanTypeRepository mealPlanTypeRepository;
    private final ExternalRecipeService externalRecipeService;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final ExternalTrackingService externalTrackingService;

    public MealPlanEntryCommandServiceImpl(
            MealPlanRepository mealPlanRepository,
            MealPlanTypeRepository mealPlanTypeRepository,
            ExternalRecipeService externalRecipeService,
            MealPlanEntryRepository mealPlanEntryRepository,
            ExternalTrackingService externalTrackingService
    ) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealPlanTypeRepository = mealPlanTypeRepository;
        this.externalRecipeService = externalRecipeService;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.externalTrackingService = externalTrackingService;
    }

    @Override
    @Transactional
    public int handle(CreateMealPlanEntryCommand command) {
        logger.info("Handling CreateMealPlanEntryCommand: mealPlanId={}, recipeId={}, type={}, day={}",
                command.mealPlanId(), command.recipeId(), command.type(), command.day());

        // ============================================
        // 1) CARGAR Y VALIDAR MEAL PLAN
        // ============================================
        var plan = mealPlanRepository.findById(command.mealPlanId())
                .orElseThrow(() -> {
                    logger.error("MealPlan not found for id {}", command.mealPlanId());
                    return new IllegalArgumentException("MealPlan not found");
                });

        // ============================================
        // 2) VALIDAR TIPO DE COMIDA Y DÍA
        // ============================================
        var mealTypeEnum = MealPlanTypes.valueOf(command.type());
        if (command.day() < 1 || command.day() > 7) {
            logger.error("Invalid day: {} (must be 1..7)", command.day());
            throw new IllegalArgumentException("day must be between 1 and 7");
        }

        var mealType = mealPlanTypeRepository.findByType(mealTypeEnum)
                .orElseThrow(() -> {
                    logger.error("MealPlanType not seeded: {}", command.type());
                    return new IllegalArgumentException("MealPlanType not seeded: " + command.type());
                });

        // ============================================
        // 3) VALIDAR RECIPE Y OBTENER MACROS (USANDO FEIGN)
        // ============================================
        var optRecipe = externalRecipeService.fetchRecipeById(command.recipeId());
        if (optRecipe.isEmpty()) {
            logger.error("Recipe not found in Recipe BC for id {}", command.recipeId());
            throw new IllegalArgumentException("Recipe not found");
        }

        var nutrition = externalRecipeService.fetchNutrition(command.recipeId());
        logger.info("Fetched nutrition for recipe {} -> calories={}, carbs={}, proteins={}, fats={}",
                command.recipeId(),
                nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats()
        );

        // ============================================
        // 4) CREAR Y PERSISTIR MEAL PLAN ENTRY
        // ============================================
        var entry = new MealPlanEntry(new RecipeId(command.recipeId()), mealType, command.day(), plan);
        var savedEntry = mealPlanEntryRepository.save(entry);
        logger.info("Saved MealPlanEntry with id {}", savedEntry.getId());

        // ============================================
        // 5) ACTUALIZAR MACROS DEL MEAL PLAN
        // ============================================
        plan.addEntry(savedEntry);
        plan.addNutrition(nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats());
        mealPlanRepository.save(plan);
        logger.info("MealPlan {} saved with updated macros", plan.getId());

        // ============================================
        // 6) SINCRONIZAR CON TRACKING (SI APLICA) - USANDO FEIGN
        // ============================================
        syncWithTracking(plan, savedEntry, mealTypeEnum, command);

        return savedEntry.getId();
    }

    /**
     * Sincroniza la entrada del meal plan con el tracking del usuario.
     * Solo se ejecuta si el meal plan está asignado a un usuario (no es template).
     * ADAPTADO PARA USAR FEIGN EN LUGAR DE ACL
     */
    private void syncWithTracking(
            pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan plan,
            MealPlanEntry savedEntry,
            MealPlanTypes mealTypeEnum,
            CreateMealPlanEntryCommand command
    ) {
        try {
            // Validar que el meal plan tenga un profileId (no es template)
            if (plan.getProfileId() == null) {
                logger.info("MealPlan {} has no profileId (it's a template), skipping tracking sync", plan.getId());
                return;
            }

            // Extraer profileId
            Long profileId = extractProfileId(plan.getProfileId());
            if (profileId == null || profileId == 0) {
                logger.warn("Could not extract valid profileId from MealPlan {}, skipping tracking sync", plan.getId());
                return;
            }

            logger.info("Extracted profileId={} from MealPlan {}", profileId, plan.getId());

            // Sincronizar con tracking usando Feign
            externalTrackingService.syncMealPlanEntryToTracking(
                    profileId,
                    command.recipeId(),
                    mealTypeEnum.name(),
                    command.day()
            );

            logger.info("Successfully synced meal-plan entry {} to tracking for profileId={}",
                    savedEntry.getId(), profileId);

        } catch (Exception ex) {
            // No romper el flujo principal si falla la sincronización
            logger.error("Error during tracking sync for mealPlanEntry {}: {}",
                    savedEntry.getId(), ex.getMessage(), ex);
        }
    }

    /**
     * Extrae el ID de perfil del value object UserProfileId.
     * Retorna Long (convierte de int a long).
     */
    private Long extractProfileId(Object profileVo) {
        if (profileVo == null) {
            return null;
        }

        // Caso directo: si es el VO UserProfileId
        if (profileVo instanceof UserProfileId) {
            int id = ((UserProfileId) profileVo).userProfileId();
            return id > 0 ? Long.valueOf(id) : null;
        }

        // Fallback: intentar obtener por reflection
        try {
            var method = profileVo.getClass().getMethod("userProfileId");
            Object val = method.invoke(profileVo);
            if (val instanceof Number) {
                long id = ((Number) val).longValue();
                return id > 0 ? id : null;
            }
        } catch (Exception e) {
            logger.warn("Could not extract profileId from {}: {}", profileVo.getClass().getName(), e.getMessage());
        }

        return null;
    }
}
