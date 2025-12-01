package pe.edu.upc.mealplan_service.mealplan.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.ExternalMealPlanRecipeService;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanEntryCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanTypes;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.RecipeId;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanEntryCommandService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanEntryRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanTypeRepository;
import pe.edu.upc.center.jameoFit.tracking.interfaces.acl.TrackingContextFacade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Service
public class MealPlanEntryCommandServiceImpl implements MealPlanEntryCommandService {

    private static final Logger logger = LoggerFactory.getLogger(MealPlanEntryCommandServiceImpl.class);

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanTypeRepository mealPlanTypeRepository;
    private final ExternalMealPlanRecipeService externalMealPlanRecipeService;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final TrackingContextFacade trackingContextFacade;

    public MealPlanEntryCommandServiceImpl(
            MealPlanRepository mealPlanRepository,
            MealPlanTypeRepository mealPlanTypeRepository,
            ExternalMealPlanRecipeService externalMealPlanRecipeService,
            MealPlanEntryRepository mealPlanEntryRepository,
            TrackingContextFacade trackingContextFacade
    ) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealPlanTypeRepository = mealPlanTypeRepository;
        this.externalMealPlanRecipeService = externalMealPlanRecipeService;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.trackingContextFacade = trackingContextFacade;
    }

    @Override
    @Transactional
    public int handle(CreateMealPlanEntryCommand command) {
        logger.info("Handling CreateMealPlanEntryCommand: mealPlanId={}, recipeId={}, type={}, day={}",
                command.mealPlanId(), command.recipeId(), command.type(), command.day());

        // 1) Cargar el plan
        var plan = mealPlanRepository.findById(command.mealPlanId())
                .orElseThrow(() -> {
                    logger.error("MealPlan not found for id {}", command.mealPlanId());
                    return new IllegalArgumentException("MealPlan not found");
                });

        // 2) Validar tipo & día
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

        // 3) Validar receta y obtener macros vía ACL
        var optRecipe = externalMealPlanRecipeService.fetchRecipeById(command.recipeId());
        if (optRecipe.isEmpty()) {
            logger.error("Recipe not found in Recipe BC for id {}", command.recipeId());
            throw new IllegalArgumentException("Recipe not found");
        }
        logger.info("Recipe {} exists in Recipe BC", command.recipeId());

        var nutrition = externalMealPlanRecipeService.fetchNutrition(command.recipeId());
        logger.info("Fetched nutrition for recipe {} -> calories={}, carbs={}, proteins={}, fats={}",
                command.recipeId(),
                nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats()
        );

        // 4) Crear entry (instancia)
        var entry = new MealPlanEntry(new RecipeId(command.recipeId()), mealType, command.day(), plan);

        // 5) Persistir ENTRY explícitamente para obtener su ID
        var savedEntry = mealPlanEntryRepository.save(entry); // <- now savedEntry.getId() should be > 0
        logger.info("Saved MealPlanEntry with id {}", savedEntry.getId());

        // 6) Agregar entrada al agregado y actualizar macros del plan
        plan.addEntry(savedEntry);
        plan.addNutrition(nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats());

        // SAFE LOG: no acceder a campos privados directamente. Intentamos leer los valores por métodos comunes; si no, usamos toString()
        Object macrosVo = plan.getMacros();
        Double cal = tryGetDoubleFromMacros(macrosVo, new String[]{"calories", "getCalories"});
        Double carbs = tryGetDoubleFromMacros(macrosVo, new String[]{"carbs", "getCarbs"});
        Double proteins = tryGetDoubleFromMacros(macrosVo, new String[]{"proteins", "getProteins"});
        Double fats = tryGetDoubleFromMacros(macrosVo, new String[]{"fats", "getFats"});

        if (cal != null && carbs != null && proteins != null && fats != null) {
            logger.info("Updated meal plan id={} macros after adding entry: calories={}, carbs={}, proteins={}, fats={}",
                    plan.getId(), cal, carbs, proteins, fats);
        } else {
            logger.info("Updated meal plan id={} macros after adding entry: macrosVo={}", plan.getId(), macrosVo);
        }

        // 7) Persistir el agregado (actualiza totales, cascade si aplica)
        mealPlanRepository.save(plan);
        logger.info("MealPlan {} saved with new totals", plan.getId());

        // --- 8) Sincronizar con Tracking BC (opcional, no rompe si falla) ---
        try {
            Object profileVo = plan.getProfileId();
            if (profileVo == null) {
                logger.warn("MealPlan.profileId is null for mealPlanId={}, skipping tracking sync", plan.getId());
            } else {
                Long profileIdLong = null;
                try {
                    Method m = profileVo.getClass().getMethod("getUserProfileId");
                    Object val = m.invoke(profileVo);
                    if (val instanceof Number) profileIdLong = ((Number) val).longValue();
                    else if (val instanceof String) profileIdLong = Long.valueOf((String) val);
                } catch (Exception ex) {
                    // fallback
                    profileIdLong = tryExtractProfileId(profileVo);
                }

                if (profileIdLong == null) {
                    logger.warn("Could not extract profileId from MealPlan.profileId (class={}), skipping tracking sync", profileVo.getClass().getName());
                } else {
                    logger.info("Extracted profileId={} from MealPlan", profileIdLong);
                    var optTracking = trackingContextFacade.getTrackingByUserId(profileIdLong);
                    if (optTracking.isPresent()) {
                        var tracking = optTracking.get();
                        long trackingId = tracking.getId();
                        logger.info("Found Tracking id={} for profileId={}, will sync entry {}", trackingId, profileIdLong, savedEntry.getId());

                        // ----- MAPEADO TOLERANTE ENTRE ENUMS -----
                        // Intentamos mapear mealTypeEnum (mealplan package) a la constante del enum del tracking ignorando case.
                        Object mappedTrackingEnum = mapMealPlanTypeToTrackingEnum(mealTypeEnum);
                        if (mappedTrackingEnum == null) {
                            logger.warn("Could not map MealPlanTypes '{}' (mealplan) to tracking enum; skipping tracking sync for entry {}", mealTypeEnum.name(), savedEntry.getId());
                        } else {
                            // cast seguro al tipo del tracking enum
                            pe.edu.upc.center.jameoFit.tracking.domain.model.valueobjects.MealPlanTypes trackingEnum =
                                    (pe.edu.upc.center.jameoFit.tracking.domain.model.valueobjects.MealPlanTypes) mappedTrackingEnum;

                            var trackingMealPlanTypeEntity = new pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.MealPlanType(trackingEnum);

                            var createTrackingEntryCmd = new pe.edu.upc.center.jameoFit.tracking.domain.model.commands.CreateMealPlanEntryToTrackingCommand(
                                    new pe.edu.upc.center.jameoFit.tracking.domain.model.valueobjects.UserId(profileIdLong),
                                    trackingId,
                                    new pe.edu.upc.center.jameoFit.tracking.domain.model.valueobjects.RecipeId((long) command.recipeId()),
                                    trackingMealPlanTypeEntity,
                                    command.day()
                            );

                            try {
                                trackingContextFacade.addMealPlanEntryToTracking(createTrackingEntryCmd);
                                logger.info("Successfully synced meal-plan entry {} -> tracking {}", savedEntry.getId(), trackingId);
                            } catch (Exception ex) {
                                logger.error("Error while syncing entry {} to tracking {}: {}", savedEntry.getId(), trackingId, ex.getMessage(), ex);
                            }
                        }
                    } else {
                        logger.info("No Tracking found for profileId={} (optional sync skipped). If you want automatic creation, call createCompleteTrackingSetup()", profileIdLong);
                    }
                }
            }
        } catch (Exception ex) {
            // no rompes la creación del meal plan; solo logueamos.
            logger.error("Unexpected error during tracking sync for mealPlanEntry {}: {}", savedEntry.getId(), ex.getMessage(), ex);
        }

        // 9) devolver el id del entry guardado (ya no 0)
        return savedEntry.getId();
    }

    /**
     * Intentar extraer un ID numérico desde el VO embebido del MealPlan (UserProfileId).
     * Soporta varios nombres comunes de getters/fields: getUserProfileId, userProfileId, getId, id.
     * Devuelve null si no logra extraer.
     */
    private Long tryExtractProfileId(Object profileVo) {
        if (profileVo == null) return null;
        String[] methodCandidates = new String[] { "getUserProfileId", "userProfileId", "getId", "id", "getUserId", "userId" };
        for (String mName : methodCandidates) {
            try {
                Method m = profileVo.getClass().getMethod(mName);
                Object val = m.invoke(profileVo);
                if (val instanceof Number) return ((Number) val).longValue();
                if (val instanceof String) return Long.valueOf((String) val);
            } catch (NoSuchMethodException ignored) {
                // intentar siguiente candidate
            } catch (Exception e) {
                // cualquier fallo lo ignoramos y seguimos probando
            }
        }
        // Si no hay métodos, intentar buscar campo público (fallback)
        try {
            String[] fieldCandidates = new String[] { "userProfileId", "id", "userId" };
            for (String fName : fieldCandidates) {
                try {
                    var field = profileVo.getClass().getDeclaredField(fName);
                    field.setAccessible(true);
                    Object val = field.get(profileVo);
                    if (val instanceof Number) return ((Number) val).longValue();
                    if (val instanceof String) return Long.valueOf((String) val);
                } catch (NoSuchFieldException ignored) { }
            }
        } catch (Throwable ignored) { }

        return null;
    }

    /**
     * Intenta leer un valor double desde el VO macros por varios nombres de método conocidos.
     * Retorna null si no lo encuentra.
     */
    private Double tryGetDoubleFromMacros(Object macrosVo, String[] candidateMethodNames) {
        if (macrosVo == null) return null;

        for (String mName : candidateMethodNames) {
            try {
                Method m = macrosVo.getClass().getMethod(mName);
                Object val = m.invoke(macrosVo);
                if (val instanceof Number) return ((Number) val).doubleValue();
                if (val instanceof String) return Double.valueOf((String) val);
            } catch (NoSuchMethodException ignored) {
            } catch (Exception e) {
                // si falla intentamos siguiente candidate
            }
        }

        // Try direct field access if there is a field with the expected name
        try {
            String base = candidateMethodNames[0].replace("get", "");
            String fName = base.substring(0,1).toLowerCase() + base.substring(1);
            Field field = macrosVo.getClass().getDeclaredField(fName);
            field.setAccessible(true);
            Object val = field.get(macrosVo);
            if (val instanceof Number) return ((Number) val).doubleValue();
            if (val instanceof String) return Double.valueOf((String) val);
        } catch (NoSuchFieldException ignored) {
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    /**
     * Mapea de forma tolerante el enum MealPlanTypes del bounded context mealplan
     * al enum MealPlanTypes del bounded context tracking (ignorando mayúsculas/minúsculas).
     * Retorna null si no encuentra match.
     */
    private Object mapMealPlanTypeToTrackingEnum(MealPlanTypes source) {
        if (source == null) return null;
        try {
            Class<?> trackingEnumClass = pe.edu.upc.center.jameoFit.tracking.domain.model.valueobjects.MealPlanTypes.class;
            Object[] constants = trackingEnumClass.getEnumConstants();
            if (constants == null) return null;
            String sourceName = source.name();

            // 1) exact match ignore case
            for (Object c : constants) {
                if (((Enum<?>) c).name().equalsIgnoreCase(sourceName)) return c;
            }

            // 2) try with uppercase/lowercase normalization
            for (Object c : constants) {
                if (((Enum<?>) c).name().equals(sourceName.toUpperCase())) return c;
            }

            // 3) try capitalized vs others (Breakfast vs BREAKFAST etc)
            String cap = sourceName.substring(0,1).toUpperCase() + sourceName.substring(1).toLowerCase();
            for (Object c : constants) {
                if (((Enum<?>) c).name().equals(cap)) return c;
            }

            // 4) try matching by numeric/value names if you use different naming (optional, ignored here)

            // no match
            return null;
        } catch (Exception e) {
            logger.error("Error mapping mealPlan enum to tracking enum: {}", e.getMessage(), e);
            return null;
        }
    }
}
