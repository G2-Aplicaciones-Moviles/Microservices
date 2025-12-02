package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.valueobjects.ServiceType;

import java.util.Date;

public record CreateNutritionistPatientResource(
        Integer nutritionistId,
        Long patientUserId,
        ServiceType serviceType,
        Date startDate,
        Date scheduledAt
) {}