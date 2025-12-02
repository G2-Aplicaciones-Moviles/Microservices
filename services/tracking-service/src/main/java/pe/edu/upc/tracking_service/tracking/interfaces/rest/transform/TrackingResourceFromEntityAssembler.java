// TrackingResourceFromEntityAssembler.java
package pe.edu.upc.tracking_service.tracking.interfaces.rest.transform;

import pe.edu.upc.tracking_service.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.TrackingResource;

public class TrackingResourceFromEntityAssembler {
    public static TrackingResource toResource(Tracking entity) {
        var consumedMacros = MacronutrientValuesResourceFromEntityAssembler.toResource(entity.getConsumedMacros());
        var mealPlanEntries = entity.getMealPlanEntries().getMealPlanEntries().stream()
                .map(MealPlanEntriesResourceFromEntityAssembler::toResource)
                .toList();

        return new TrackingResource(
                entity.getId().intValue(),
                entity.getUserId().userId(),
                entity.getDate(),
                consumedMacros,
                mealPlanEntries
        );
    }
}