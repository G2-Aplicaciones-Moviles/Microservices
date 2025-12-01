package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.center.jameoFit.tracking.domain.model.dto.UserProfileDto;

import java.time.LocalDate;
import java.time.Period;

/**
 * Servicio utilitario (stateless) para cálculo de BMR, TDEE y macronutrientes.
 * - BMR: Mifflin-St Jeor
 * - TDEE: BMR * activityFactor
 * - Ajuste por objetivo: +300 / -300 / 0
 * - Proteínas: g/kg
 * - Grasas: % kcal
 * - Carbohidratos: resto (g)
 */
public final class CalorieCalculatorService {

    private CalorieCalculatorService() {}

    public static int calculateAgeFromBirthDate(String birthDateIso) {
        if (birthDateIso == null || birthDateIso.isBlank()) {
            throw new IllegalArgumentException("birthDate is required and must be in ISO format yyyy-MM-dd");
        }
        LocalDate birth = LocalDate.parse(birthDateIso);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public static double calculateBmr(String gender, double weightKg, double heightMeters, int ageYears) {
        double heightCm = heightMeters * 100.0;
        if (gender == null) gender = "";
        if (gender.equalsIgnoreCase("MALE") || gender.equalsIgnoreCase("M")) {
            return 88.36 + (13.4 * weightKg) + (4.8 * heightCm) - (5.7 * ageYears);
        } else {
            return 447.6 + (9.2 * weightKg) + (3.1 * heightCm) - (4.3 * ageYears);
        }
    }

    public static double calculateMaintenanceCalories(UserProfileDto profile) {
        int age = calculateAgeFromBirthDate(profile.birthDate());
        double bmr = calculateBmr(profile.gender(), profile.weightKg(), profile.heightMeters(), age);
        return bmr * profile.activityFactor();
    }

    public static double calculateTargetCalories(UserProfileDto profile) {
        double maintenance = calculateMaintenanceCalories(profile);
        String obj = profile.objectiveName();
        if (obj == null) return maintenance;

        String lower = obj.trim().toLowerCase();
        return switch (lower) {
            case "ganancia", "ganancia muscular", "ganar peso", "gain" -> maintenance + 300.0;
            case "perdida", "pérdida", "perdida de peso", "perder peso", "loss" -> maintenance - 300.0;
            case "mantenimiento", "mantener", "maintenance" -> maintenance;
            default -> maintenance;
        };
    }

    /**
     * Devuelve MacronutrientValues con:
     * - calories = total kcal objetivo
     * - carbs/proteins/fats = gramos
     */
    public static MacronutrientValues calculateTargetMacronutrients(UserProfileDto profile) {
        double calories = calculateTargetCalories(profile);

        // Protein: grams per kg (configurable)
        double proteinPerKg = 1.6;
        double proteinGrams = proteinPerKg * profile.weightKg();
        double proteinCalories = proteinGrams * 4.0;

        // Fat: 25% calories
        double fatCalories = calories * 0.25;
        double fatGrams = fatCalories / 9.0;

        // Carbs: remainder
        double remainingCalories = calories - (proteinCalories + fatCalories);
        double carbsGrams = Math.max(0.0, remainingCalories / 4.0);

        return new MacronutrientValues(calories, carbsGrams, proteinGrams, fatGrams);
    }
}
