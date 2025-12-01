package pe.edu.upc.profiles_service.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.ExternalIngredientService;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Allergy;
import pe.edu.upc.profiles_service.profiles.domain.model.valueobjects.Ingredient;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.AllergyRepository;

import java.util.List;

/**
 * Servicio de comandos para gestionar alergias
 */
@Service
public class AllergyCommandService {

    private final AllergyRepository allergyRepository;
    private final ExternalIngredientService externalIngredientService;

    public AllergyCommandService(AllergyRepository allergyRepository,
                                 ExternalIngredientService externalIngredientService) {
        this.allergyRepository = allergyRepository;
        this.externalIngredientService = externalIngredientService;
    }

    /**
     * Crea una nueva alergia validando que los ingredientes existan en recipes-service
     */
    @Transactional
    public Allergy createAllergy(Allergy allergy) {
        // Validar que todos los ingredientes relacionados existan en recipes-service
        for (Ingredient ingredient : allergy.getRelatedIngredients()) {
            String name = ingredient.getName();
            if (!externalIngredientService.existsByName(name)) {
                throw new IllegalArgumentException("Ingrediente no registrado: " + name);
            }
        }

        // Asegurar que el ID sea 0 para crear un nuevo registro
        allergy.setId(0);
        return allergyRepository.save(allergy);
    }

    /**
     * Obtiene todas las alergias
     */
    public List<Allergy> getAllAllergies() {
        return allergyRepository.findAll();
    }

    /**
     * Elimina una alergia por ID
     */
    @Transactional
    public void deleteAllergy(int id) {
        Long longId = (long) id;
        if (!allergyRepository.existsById(longId)) {
            throw new IllegalArgumentException("Alergia no encontrada con ID: " + id);
        }
        allergyRepository.deleteById(longId);
    }
}

