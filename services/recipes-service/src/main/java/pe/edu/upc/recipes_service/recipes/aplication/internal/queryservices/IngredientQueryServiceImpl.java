package pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Ingredient;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllIngredientsQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetIngredientsByIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.services.IngredientQueryService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.IngredientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientQueryServiceImpl implements IngredientQueryService {

    private final IngredientRepository ingredientRepository;

    public IngredientQueryServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> handle(GetAllIngredientsQuery query) {
        return ingredientRepository.findAll();
    }

    @Override
    public Optional<Ingredient> handle(GetIngredientsByIdQuery query) {
        return ingredientRepository.findById(query.ingredientId());
    }
}
