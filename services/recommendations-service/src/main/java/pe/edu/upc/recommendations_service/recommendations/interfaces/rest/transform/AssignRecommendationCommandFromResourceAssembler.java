package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.transform;

import pe.edu.upc.recommendations_service.recommendations.domain.model.commands.AssignRecommendationCommand;
import pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources.AssignRecommendationResource;

public class AssignRecommendationCommandFromResourceAssembler {

    public static AssignRecommendationCommand toCommandFromResource(AssignRecommendationResource resource) {
        return new AssignRecommendationCommand(
                resource.userId(),
                resource.templateId(),
                resource.reason(),
                resource.notes(),
                resource.timeOfDay(),
                resource.score(),
                resource.status()
        );
    }
}
