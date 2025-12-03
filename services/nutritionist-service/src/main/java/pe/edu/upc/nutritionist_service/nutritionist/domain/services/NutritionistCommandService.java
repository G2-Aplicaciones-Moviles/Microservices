package pe.edu.upc.nutritionist_service.nutritionist.domain.services;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.CreateNutritionistCommand;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.UpdateNutritionistCommand;

import java.util.Optional;

public interface NutritionistCommandService {
    Nutritionist handle(CreateNutritionistCommand command);
    Optional<Nutritionist> handle(UpdateNutritionistCommand command);
}
