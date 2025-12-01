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

    /**
     * Handles the creation of a new meal plan entry.
     *
     * @param command the command containing entry details
     * @return the ID of the created entry
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    @Transactional
    public int handle(CreateMealPlanEntryCommand command) {
        logger.info("Creating meal plan entry: mealPlanId={}, recipeId={}, type={}, day={}",
                command.mealPlanId(), command.recipeId(), command.type(), command.day());

        var plan = mealPlanRepository.findById(command.mealPlanId())
                .orElseThrow(() -> {
                    logger.error("MealPlan not found: {}", command.mealPlanId());
                    return new IllegalArgumentException("MealPlan not found");
                });

        var mealTypeEnum = MealPlanTypes.valueOf(command.type());
        if (command.day() < 1 || command.day() > 7) {
            logger.error("Invalid day: {}", command.day());
            throw new IllegalArgumentException("Day must be between 1 and 7");
        }

        var mealType = mealPlanTypeRepository.findByType(mealTypeEnum)
                .orElseThrow(() -> {
                    logger.error("MealPlanType not found: {}", command.type());
                    return new IllegalArgumentException("MealPlanType not seeded: " + command.type());
                });

        var optRecipe = externalRecipeService.fetchRecipeById(command.recipeId());
        if (optRecipe.isEmpty()) {
            logger.error("Recipe not found: {}", command.recipeId());
            throw new IllegalArgumentException("Recipe not found");
        }

        var nutrition = externalRecipeService.fetchNutrition(command.recipeId());
        logger.debug("Recipe nutrition: calories={}, carbs={}, proteins={}, fats={}",
                nutrition.calories(), nutrition.carbs(), nutrition.proteins(), nutrition.fats());

        var entry = new MealPlanEntry(new RecipeId(command.recipeId()), mealType, command.day(), plan);
        var savedEntry = mealPlanEntryRepository.save(entry);
        logger.info("Saved MealPlanEntry with id: {}", savedEntry.getId());

        plan.addEntry(savedEntry);
        plan.addNutrition(nutrition.calories(), nutrition.carbs(),
                nutrition.proteins(), nutrition.fats());
        mealPlanRepository.save(plan);

        logger.info("Updated MealPlan {} with new entry", plan.getId());

        validateTrackingSync(plan, savedEntry);

        return savedEntry.getId();
    }

    /**
     * Validates if tracking sync is possible for the user.
     * Logs information about tracking availability without breaking the flow.
     */
    private void validateTrackingSync(
            pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan plan,
            MealPlanEntry entry) {
        try {
            Long userId = extractProfileId(plan.getProfileId());

            if (externalTrackingService.trackingExistsForUser(userId)) {
                logger.info("Tracking exists for user {}. Entry {} can be synced if needed.",
                        userId, entry.getId());
            } else {
                logger.info("No tracking found for user {}. Entry {} saved without tracking sync.",
                        userId, entry.getId());
            }
        } catch (Exception e) {
            logger.warn("Could not validate tracking sync: {}", e.getMessage());
        }
    }

    private Long extractProfileId(Object profileVo) {
        if (profileVo == null) {
            return null;
        }

        if (profileVo instanceof UserProfileId) {
            int id = ((UserProfileId) profileVo).userProfileId();
            return id > 0 ? (long) id : null;
        }

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
