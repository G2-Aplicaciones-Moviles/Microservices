package pe.edu.upc.nutritionist_service.nutritionist.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.NutritionistPatient;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistPatientQueryService;
import pe.edu.upc.nutritionist_service.nutritionist.infrastructure.persistence.jpa.repositories.NutritionistPatientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionistPatientQueryServiceImpl implements NutritionistPatientQueryService {

    private final NutritionistPatientRepository repository;

    public NutritionistPatientQueryServiceImpl(NutritionistPatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<NutritionistPatient> findPatientsByNutritionist(Integer nutritionistId) {
        return repository.findByNutritionistId(nutritionistId);
    }

    @Override
    public List<NutritionistPatient> findNutritionistsOfPatient(Long userId) {
        return repository.findByPatientUserId(userId);
    }

    @Override
    public Optional<NutritionistPatient> findById(Long id) {
        return repository.findById(id);
    }
}
