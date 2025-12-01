package pe.edu.upc.tracking_service.tracking.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MealPlanType;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.SeedTrackingMasterDataCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.MealPlanTypes;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingMasterDataCommandService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.MacronutrientValuesRepository;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.TrackingMealPlanTypeRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class TrackingMasterDataCommandServiceImpl implements TrackingMasterDataCommandService {

    private final TrackingMealPlanTypeRepository trackingMealPlanTypeRepository;
    private final MacronutrientValuesRepository macronutrientValuesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingMasterDataCommandServiceImpl.class);

    public TrackingMasterDataCommandServiceImpl(TrackingMealPlanTypeRepository trackingMealPlanTypeRepository,
                                                MacronutrientValuesRepository macronutrientValuesRepository) {
        this.trackingMealPlanTypeRepository = trackingMealPlanTypeRepository;
        this.macronutrientValuesRepository = macronutrientValuesRepository;
    }

    @Override
    public void handle(SeedTrackingMasterDataCommand command) {
        seedMealPlanTypes();
        seedDefaultMacronutrientValues();
    }

    private void seedMealPlanTypes() {
        // Use the enum values directly instead of strings
        List<MealPlanTypes> mealPlanTypes = Arrays.asList(
                MealPlanTypes.BREAKFAST,
                MealPlanTypes.LUNCH,
                MealPlanTypes.DINNER,
                MealPlanTypes.HEALTHY
        );

        LOGGER.info("Verifying if meal plan types seeding is needed...");

        mealPlanTypes.forEach(mealPlanType -> {
            // ALTERNATIVA: Si tu repositorio tiene un método que acepta MealPlanTypes directamente
            if (!trackingMealPlanTypeRepository.existsByName(mealPlanType)) {
                var mealPlan = new MealPlanType(mealPlanType);
                trackingMealPlanTypeRepository.save(mealPlan);
                LOGGER.info("Meal plan type {} seeded successfully", mealPlanType.name());
            } else {
                LOGGER.debug("Meal plan type {} already exists", mealPlanType.name());
            }
        });

        LOGGER.info("Meal plan types seeding verification completed");
    }

    private void seedDefaultMacronutrientValues() {
        LOGGER.info("Verifying if default macronutrient values seeding is needed...");

        // Valores por defecto para diferentes objetivos
        List<MacronutrientValues> defaultMacros = Arrays.asList(
                // Mantenimiento general (2000 cal, 50% carbs, 25% proteins, 25% fats)
                new MacronutrientValues(2000.0, 250.0, 125.0, 55.6),
                // Pérdida de peso (1500 cal, 40% carbs, 35% proteins, 25% fats)
                new MacronutrientValues(1500.0, 150.0, 131.25, 41.7),
                // Ganancia muscular (2500 cal, 55% carbs, 25% proteins, 20% fats)
                new MacronutrientValues(2500.0, 343.75, 156.25, 55.6),
                // Dieta cetogénica (1800 cal, 5% carbs, 25% proteins, 70% fats)
                new MacronutrientValues(1800.0, 22.5, 112.5, 140.0)
        );

        for (MacronutrientValues macro : defaultMacros) {
            // Verificar si ya existe un valor similar
            if (!macronutrientValuesRepository.existsByCaloriesAndCarbsAndProteinsAndFats(
                    macro.getCalories(), macro.getCarbs(), macro.getProteins(), macro.getFats())) {
                macronutrientValuesRepository.save(macro);
                LOGGER.info("Default macronutrient values seeded: {} cal, {} carbs, {} proteins, {} fats",
                        macro.getCalories(), macro.getCarbs(), macro.getProteins(), macro.getFats());
            }
        }

        LOGGER.info("Default macronutrient values seeding verification completed");
    }
}