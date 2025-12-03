package pe.edu.upc.nutritionist_service.nutritionist.domain.services;

import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.queries.GetNutritionistByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface NutritionistQueryService {
    Optional<Nutritionist> handle(GetNutritionistByUserIdQuery query);
    List<Nutritionist> handleAll();
}
