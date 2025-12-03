package pe.edu.upc.nutritionist_service.nutritionist.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.queries.GetNutritionistByUserIdQuery;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistQueryService;
import pe.edu.upc.nutritionist_service.nutritionist.infrastructure.persistence.jpa.repositories.NutritionistRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionistQueryServiceImpl implements NutritionistQueryService {

    private final NutritionistRepository repository;

    public NutritionistQueryServiceImpl(NutritionistRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Nutritionist> handle(GetNutritionistByUserIdQuery query) {
        return repository.findByUserId(query.userId());
    }

    @Override
    public List<Nutritionist> handleAll() {
        return repository.findAll();
    }
}
