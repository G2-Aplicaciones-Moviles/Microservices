// MealPlanEntriesResourceFromEntityAssembler.java
package pe.edu.upc.tracking_service.tracking.interfaces.rest.transform;

import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.MealPlanEntriesResource;

public class MealPlanEntriesResourceFromEntityAssembler {
    public static MealPlanEntriesResource toResource(TrackingMealPlanEntry entity) {
        return new MealPlanEntriesResource(
                entity.getId(),
                entity.getRecipeId().recipeId(),
                entity.getMealPlanType().ToString(),
                entity.getDayNumber()
        );
    }
}