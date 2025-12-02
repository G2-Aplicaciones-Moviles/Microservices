// CreateTrackingCommandFromResourceAssembler.java
package pe.edu.upc.tracking_service.tracking.interfaces.rest.transform;

import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.CreateTrackingResource;

public class CreateTrackingCommandFromResourceAssembler {
    public static CreateTrackingCommand toCommand(CreateTrackingResource resource) {
        return new CreateTrackingCommand(new UserId(resource.userId()));
    }
}