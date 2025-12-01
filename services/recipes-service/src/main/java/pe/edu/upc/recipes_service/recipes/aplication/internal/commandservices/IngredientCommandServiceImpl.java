package pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.ExternalProfileAndTrackingService;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Ingredient;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateIngredientCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteIngredientCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.MacronutrientValuesId;
import pe.edu.upc.recipes_service.recipes.domain.services.IngredientCommandService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.IngredientRepository;

@Service
public class IngredientCommandServiceImpl implements IngredientCommandService {

    private final IngredientRepository ingredientRepository;
    private final ExternalProfileAndTrackingService externalProfileAndTrackingService;

    public IngredientCommandServiceImpl(IngredientRepository ingredientRepository, ExternalProfileAndTrackingService externalProfileAndTrackingService) {
        this.ingredientRepository = ingredientRepository;
        this.externalProfileAndTrackingService = externalProfileAndTrackingService;
    }

    @Override
    public Integer handle(CreateIngredientCommand command) {
        // Validar si ya existe un ingrediente con el mismo nombre
        if (ingredientRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("An ingredient with the name " + command.name() + " already exists.");
        }

        // Validar existencia de MacronutrientValuesId
        externalProfileAndTrackingService.validateMacronutrientValuesExists(new MacronutrientValuesId(command.macronutrientValuesId()));

        // Crear ingrediente
        var ingredient = new Ingredient(
                command.name(),
                command.calories(),
                command.carbohydrates(),
                command.proteins(),
                command.fats(),
                command.macronutrientValuesId()
        );

        try {
            this.ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving ingredient: " + e.getMessage());
        }

        return ingredient.getId();
    }

    @Override
    public void handle(DeleteIngredientCommand command) {
        if (!ingredientRepository.existsById(command.ingredientId())) {
            throw new IllegalArgumentException("Ingredient not found");
        }

        try {
            this.ingredientRepository.deleteById(command.ingredientId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting ingredient: " + e.getMessage());
        }
    }
}