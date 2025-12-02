package pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.ServiceType;

import java.util.Date;

public record CreateNutritionistPatientCommand(
        Integer nutritionistId,
        Long patientUserId,
        ServiceType serviceType,
        Date startDate,
        Date scheduledAt
) {}
