package pe.edu.upc.nutritionist_service.nutritionist.domain.services;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.NutritionistPatient;

import java.util.List;
import java.util.Optional;

public interface NutritionistPatientQueryService {

    /**
     * Lista a todos los pacientes relacionados con un nutricionista
     */
    List<NutritionistPatient> findPatientsByNutritionist(Integer nutritionistId);

    /**
     * Lista todos los nutricionistas con los que un usuario tiene relación
     * (útil si el paciente tiene múltiples nutricionistas o historial)
     */
    List<NutritionistPatient> findNutritionistsOfPatient(Long userId);

    /**
     * Obtener una relación específica nutricionista–paciente
     */
    Optional<NutritionistPatient> findById(Long id);
}
