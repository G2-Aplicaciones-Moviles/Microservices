package pe.edu.upc.tracking_service.tracking.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetAllMealsQuery;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingCommandService;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingQueryService;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.CreateMealPlanEntryResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.MealPlanEntriesResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.UpdateMealPlanEntryResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.CreateMealPlanEntryCommandFromResourceAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.MealPlanEntriesResourceFromEntityAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.RemoveMealPlanEntryCommandFromResourceAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.UpdateMealPlanEntryCommandFromResourceAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/meal-plan-entries", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Meal Plan Entries", description = "Meal Plan Entries Management Endpoints")
public class MealPlanEntriesController {

    private final TrackingQueryService trackingQueryService;
    private final TrackingCommandService trackingCommandService;

    public MealPlanEntriesController(TrackingQueryService trackingQueryService, TrackingCommandService trackingCommandService) {
        this.trackingQueryService = trackingQueryService;
        this.trackingCommandService = trackingCommandService;
    }

    @Operation(
            summary = "Add a meal plan entry to tracking",
            description = "Add a new meal plan entry to a user's tracking",
            operationId = "createMealPlanEntry",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RuntimeException.class)
                            )
                    )
            }
    )
    @PostMapping("/{trackingId}")
    public ResponseEntity<Void> createMealPlanEntry(
            @PathVariable Long trackingId,
            @RequestBody CreateMealPlanEntryResource resource) {
        try {
            var createMealPlanEntryCommand = CreateMealPlanEntryCommandFromResourceAssembler.toCommand(resource, trackingId);
            this.trackingCommandService.handle(createMealPlanEntryCommand);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get all meals",
            description = "Fetch all meal plan entries from all trackings",
            operationId = "getAllMeals",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MealPlanEntriesResource.class)
                            )
                    )
            }
    )
    @GetMapping("/tracking/{trackingId}")
    public ResponseEntity<List<MealPlanEntriesResource>> getAllMeals(@PathVariable Long trackingId) {
        var getAllMealsQuery = new GetAllMealsQuery(trackingId);
        var mealPlanEntries = this.trackingQueryService.handle(getAllMealsQuery);

        var mealPlanEntriesResources = mealPlanEntries.stream()
                .map(MealPlanEntriesResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(mealPlanEntriesResources);
    }

    @Operation(
            summary = "Update a meal plan entry",
            description = "Update an existing meal plan entry in tracking",
            operationId = "updateMealPlanEntry",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Meal plan entry not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    )
            }
    )
    @PutMapping("/{mealPlanEntryId}")
    public ResponseEntity<Void> updateMealPlanEntry(
            @PathVariable Long mealPlanEntryId,
            @RequestBody UpdateMealPlanEntryResource resource) {
        try {
            var updateMealPlanEntryCommand = UpdateMealPlanEntryCommandFromResourceAssembler.toCommand(resource, mealPlanEntryId);
            var optionalTracking = this.trackingCommandService.handle(updateMealPlanEntryCommand);

            if (optionalTracking.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Remove a meal plan entry",
            description = "Remove a meal plan entry from tracking",
            operationId = "removeMealPlanEntry",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    )
            }
    )
    @DeleteMapping("/tracking/{trackingId}/entry/{mealPlanEntryId}")
    public ResponseEntity<Void> removeMealPlanEntry(
            @PathVariable Long trackingId,
            @PathVariable Long mealPlanEntryId) {
        try {
            var removeMealPlanEntryCommand = RemoveMealPlanEntryCommandFromResourceAssembler.toCommand(trackingId, mealPlanEntryId);
            this.trackingCommandService.handle(removeMealPlanEntryCommand);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}