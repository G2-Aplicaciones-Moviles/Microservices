package pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands;

public record UpdateAvailabilityCommand(
        Long userId,
        boolean acceptingNewPatients
) {}
