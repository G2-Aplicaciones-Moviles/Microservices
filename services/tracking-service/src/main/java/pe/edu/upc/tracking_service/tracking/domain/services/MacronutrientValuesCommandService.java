package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateMacronutrientValuesCommand;

public interface MacronutrientValuesCommandService {
    Long handle(CreateMacronutrientValuesCommand command);
}
