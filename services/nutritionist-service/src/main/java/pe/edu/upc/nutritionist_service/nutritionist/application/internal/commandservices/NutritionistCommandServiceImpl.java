package pe.edu.upc.nutritionist_service.nutritionist.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.ExternalUserService;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.aggregates.Nutritionist;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.CreateNutritionistCommand;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.UpdateNutritionistCommand;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistCommandService;
import pe.edu.upc.nutritionist_service.nutritionist.infrastructure.persistence.jpa.repositories.NutritionistRepository;

import java.util.Optional;

@Service
@Transactional
public class NutritionistCommandServiceImpl implements NutritionistCommandService {

    private final NutritionistRepository repository;
    private final ExternalUserService externalUserService;

    public NutritionistCommandServiceImpl(
            NutritionistRepository repository,
            ExternalUserService externalUserService) {
        this.repository = repository;
        this.externalUserService = externalUserService;
    }

    /**
     * Handles the creation of a new nutritionist.
     * Validates that the user exists in IAM service before creating the nutritionist profile.
     *
     * @param command the command containing nutritionist details
     * @return the created nutritionist
     * @throws IllegalArgumentException if the user does not exist in IAM
     */
    @Override
    public Nutritionist handle(CreateNutritionistCommand command) {
        if (!externalUserService.userExists(command.userId())) {
            throw new IllegalArgumentException(
                    "User with ID " + command.userId() + " does not exist in IAM service");
        }

        var nutritionist = new Nutritionist(
                command.userId(),
                command.fullName(),
                command.licenseNumber(),
                command.specialty(),
                command.yearsExperience(),
                command.acceptingNewPatients(),
                command.bio(),
                command.profilePictureUrl()
        );
        return repository.save(nutritionist);
    }

    @Override
    public Optional<Nutritionist> handle(UpdateNutritionistCommand command) {
        return repository.findById(command.nutritionistId())
                .map(existing -> {
                    existing.updateInfo(
                            command.fullName(),
                            command.bio(),
                            command.profilePictureUrl(),
                            command.acceptingNewPatients(),
                            command.yearsExperience()
                    );
                    return repository.save(existing);
                });
    }
}
