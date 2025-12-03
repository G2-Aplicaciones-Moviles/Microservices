package pe.edu.upc.nutritionist_service.nutritionist.domain.services;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.NutritionistPatient;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.ApprovePatientCommand;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.CreateNutritionistPatientCommand;

import java.util.Optional;

public interface NutritionistPatientCommandService {

    /**
     * Crea una relación nutricionista–paciente (solicitud de paciente)
     */
    NutritionistPatient handle(CreateNutritionistPatientCommand command);

    /**
     * Aprueba una solicitud pendiente
     */
    Optional<NutritionistPatient> handle(ApprovePatientCommand command);
}
