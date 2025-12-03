// CreateMealPlanEntryCommandFromResourceAssembler.java
package pe.edu.upc.tracking_service.tracking.interfaces.rest.transform;

import pe.edu.upc.tracking_service.tracking.domain.model.Entities.MealPlanType;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.CreateMealPlanEntryToTrackingCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.MealPlanTypes;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.RecipeId;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.CreateMealPlanEntryResource;

public class CreateMealPlanEntryCommandFromResourceAssembler {
    public static CreateMealPlanEntryToTrackingCommand toCommand(CreateMealPlanEntryResource resource, Long trackingId) {
        return new CreateMealPlanEntryToTrackingCommand(
                new UserId(resource.userId()), // Convert Long to UserId
                trackingId, // Add the missing TrackingId parameter
                new RecipeId(resource.recipeId()), // Convert Long to RecipeId
                new MealPlanType(MealPlanTypes.valueOf(resource.mealPlanType())), // Convert String to MealPlanType
                resource.dayNumber()
        );
    }
}