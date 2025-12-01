package pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeTypeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.recipes.domain.services.RecipeTypeCommandService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.RecipeTypeRepository;

@Service
public class RecipeTypeCommandServiceImpl implements RecipeTypeCommandService {

    private final RecipeTypeRepository recipeTypeRepository;

    public RecipeTypeCommandServiceImpl(RecipeTypeRepository recipeTypeRepository) {
        this.recipeTypeRepository = recipeTypeRepository;
    }

    @Override
    public Long handle(CreateRecipeTypeCommand command) {
        if (recipeTypeRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Recipe type already exists with name: " + command.name());
        }

        var recipeType = new RecipeType(command.name());
        recipeTypeRepository.save(recipeType);
        return recipeType.getId();
    }
}
