package pe.edu.upc.tracking_service.tracking.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetMacronutrientValuesByIdQuery;
import pe.edu.upc.tracking_service.tracking.domain.services.MacronutrientValuesQueryService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.MacronutrientValuesRepository;

import java.util.Optional;


@Service
public class MacronutrientValuesQueryServiceImpl implements MacronutrientValuesQueryService {

    private final MacronutrientValuesRepository macronutrientValuesRepository;

    public MacronutrientValuesQueryServiceImpl(MacronutrientValuesRepository macronutrientValuesRepository) {
        this.macronutrientValuesRepository = macronutrientValuesRepository;
    }

    // Métodos existentes...

    @Override
    public Optional<MacronutrientValues> handle(GetMacronutrientValuesByIdQuery query) {
        return macronutrientValuesRepository.findById(query.macronutrientValuesId());
    }
}