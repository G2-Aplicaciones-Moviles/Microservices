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
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetConsumedMacrosQuery;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetTrackingByUserIdQuery;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingCommandService;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingQueryService;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.CreateTrackingResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.MacronutrientValuesResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.TrackingResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.CreateTrackingCommandFromResourceAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.MacronutrientValuesResourceFromEntityAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.TrackingResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tracking", description = "Tracking Management Endpoints")
public class TrackingController {

    private final TrackingQueryService trackingQueryService;
    private final TrackingCommandService trackingCommandService;

    public TrackingController(TrackingQueryService trackingQueryService, TrackingCommandService trackingCommandService) {
        this.trackingQueryService = trackingQueryService;
        this.trackingCommandService = trackingCommandService;
    }

    @Operation(summary = "Create a new tracking", description = "Create a new tracking for a user", operationId = "createTracking",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrackingResource.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RuntimeException.class)))
            })
    @PostMapping
    public ResponseEntity<TrackingResource> createTracking(@RequestBody CreateTrackingResource resource) {
        var createTrackingCommand = CreateTrackingCommandFromResourceAssembler.toCommand(resource);
        var trackingId = this.trackingCommandService.handle(createTrackingCommand);

        if (trackingId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getTrackingByUserIdQuery = new GetTrackingByUserIdQuery(new UserId(resource.userId()));
        var optionalTracking = this.trackingQueryService.handle(getTrackingByUserIdQuery);

        if (optionalTracking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var trackingResource = TrackingResourceFromEntityAssembler.toResource(optionalTracking.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(trackingResource);
    }

    @Operation(summary = "Get tracking by user ID", description = "Fetch tracking information for a specific user", operationId = "getTrackingByUserId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrackingResource.class))),
                    @ApiResponse(responseCode = "404", description = "Tracking not found")
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<TrackingResource> getTrackingByUserId(@PathVariable Long userId) {
        var getTrackingByUserIdQuery = new GetTrackingByUserIdQuery(new UserId(userId));
        var optionalTracking = this.trackingQueryService.handle(getTrackingByUserIdQuery);

        if (optionalTracking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var trackingResource = TrackingResourceFromEntityAssembler.toResource(optionalTracking.get());
        return ResponseEntity.ok(trackingResource);
    }

    @GetMapping("/macronutrients/consumed/tracking/{trackingId}")
    public ResponseEntity<MacronutrientValuesResource> getConsumedMacros(@PathVariable Long trackingId) {
        var query = new GetConsumedMacrosQuery(trackingId);
        var optionalMacros = trackingQueryService.handle(query);

        if (optionalMacros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = MacronutrientValuesResourceFromEntityAssembler.toResource(optionalMacros.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Nuevo endpoint: progress (consumed vs target) por userId.
     * Devuelve consumed (suma de meal plan entries), target (trackingGoal.targetMacros) y porcentajes.
     */
    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ProgressResource> getProgressByUserId(@PathVariable Long userId) {
        var getTrackingByUserIdQuery = new GetTrackingByUserIdQuery(new UserId(userId));
        var optionalTracking = this.trackingQueryService.handle(getTrackingByUserIdQuery);

        if (optionalTracking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var tracking = optionalTracking.get();
        var consumed = tracking.getConsumedMacros();
        var trackingGoal = tracking.getTrackingGoal();
        var target = (trackingGoal != null) ? trackingGoal.getTargetMacros() : null;

        var consumedRes = (consumed != null) ? MacronutrientValuesResourceFromEntityAssembler.toResource(consumed) : null;
        var targetRes = (target != null) ? MacronutrientValuesResourceFromEntityAssembler.toResource(target) : null;

        var progress = new ProgressResource(consumedRes, targetRes);
        return ResponseEntity.ok(progress);
    }

    // DTO interno para la respuesta de progreso
    public static record ProgressResource(MacronutrientValuesResource consumed, MacronutrientValuesResource target) { }
}
