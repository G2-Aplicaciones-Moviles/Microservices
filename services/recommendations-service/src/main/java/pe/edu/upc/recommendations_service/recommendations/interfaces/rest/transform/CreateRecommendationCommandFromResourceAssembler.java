package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.transform;

import pe.edu.upc.recommendations_service.recommendations.domain.model.commands.CreateRecommendationCommand;
import pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources.CreateRecommendationResource;

public class CreateRecommendationCommandFromResourceAssembler {
    public static CreateRecommendationCommand toCommandFromResource(CreateRecommendationResource resource) {
        return new CreateRecommendationCommand(
                resource.templateId(),
                resource.reason(),
                resource.notes(),
                resource.timeOfDay(),
                resource.score(),
                resource.status()
        );
    }
}