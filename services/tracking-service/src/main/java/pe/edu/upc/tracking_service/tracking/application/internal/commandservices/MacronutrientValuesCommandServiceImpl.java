package pe.edu.upc.tracking_service.tracking.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateMacronutrientValuesCommand;
import pe.edu.upc.tracking_service.tracking.domain.services.MacronutrientValuesCommandService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.MacronutrientValuesRepository;

@Service
public class MacronutrientValuesCommandServiceImpl implements MacronutrientValuesCommandService {

    private final MacronutrientValuesRepository macronutrientValuesRepository;

    public MacronutrientValuesCommandServiceImpl(MacronutrientValuesRepository macronutrientValuesRepository) {
        this.macronutrientValuesRepository = macronutrientValuesRepository;
    }

    @Transactional
    public Long handle(CreateMacronutrientValuesCommand command) {
        MacronutrientValues values = new MacronutrientValues(
                command.calories(),
                command.carbs(),
                command.proteins(),
                command.fats()
        );

        MacronutrientValues saved = macronutrientValuesRepository.save(values);
        return saved.getId();
    }
}
