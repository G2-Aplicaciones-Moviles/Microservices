package pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllRecipesTypesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetRecipeTypeByIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.services.RecipeTypeQueryService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.RecipeTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeTypeQueryServiceImpl implements RecipeTypeQueryService {

    private final RecipeTypeRepository recipeTypeRepository;

    public RecipeTypeQueryServiceImpl(RecipeTypeRepository recipeTypeRepository) {
        this.recipeTypeRepository = recipeTypeRepository;
    }

    @Override
    public List<RecipeType> handle(GetAllRecipesTypesQuery query) {
        return recipeTypeRepository.findAll();
    }

    @Override
    public Optional<RecipeType> handle(GetRecipeTypeByIdQuery query) {
        return recipeTypeRepository.findById(query.recipeTypeId());
    }
}
