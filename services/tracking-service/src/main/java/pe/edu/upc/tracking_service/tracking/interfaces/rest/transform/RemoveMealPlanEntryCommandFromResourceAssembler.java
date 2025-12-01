// RemoveMealPlanEntryCommandFromResourceAssembler.java
package pe.edu.upc.tracking_service.tracking.interfaces.rest.transform;

import pe.edu.upc.tracking_service.tracking.domain.model.commands.RemoveMealPlanEntryFromTrackingCommand;

public class RemoveMealPlanEntryCommandFromResourceAssembler {
    public static RemoveMealPlanEntryFromTrackingCommand toCommand(Long trackingId, Long mealPlanEntryId) {
        return new RemoveMealPlanEntryFromTrackingCommand(trackingId, mealPlanEntryId);
    }
}