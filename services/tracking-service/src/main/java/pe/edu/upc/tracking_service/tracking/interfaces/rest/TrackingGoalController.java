package pe.edu.upc.tracking_service.tracking.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tracking_service.tracking.application.internal.commandservices.MacronutrientValuesCommandServiceImpl;
import pe.edu.upc.tracking_service.tracking.application.internal.commandservices.TrackingGoalCommandServiceImpl;
import pe.edu.upc.tracking_service.tracking.domain.model.commands.UpdateTrackingGoalCommand;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetTargetMacronutrientsQuery;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetTrackingGoalByUserIdQuery;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.GoalTypes;
import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingGoalQueryService;
import pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories.MacronutrientValuesRepository;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.*;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.CreateTrackingGoalCommandFromResourceAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.MacronutrientValuesResourceFromEntityAssembler;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.TrackingGoalResourceFromEntityAssembler;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tracking-goals", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tracking Goals", description = "Tracking Goals Management Endpoints")
public class TrackingGoalController {

    private final TrackingGoalQueryService trackingGoalQueryService;
    private final MacronutrientValuesCommandServiceImpl macronutrientValuesCommandService;
    private final TrackingGoalCommandServiceImpl trackingGoalCommandService;
    private final MacronutrientValuesRepository macronutrientValuesRepository;

    public TrackingGoalController(TrackingGoalQueryService trackingGoalQueryService,
                                  MacronutrientValuesCommandServiceImpl macronutrientValuesCommandService,
                                  TrackingGoalCommandServiceImpl trackingGoalCommandService,
                                  MacronutrientValuesRepository macronutrientValuesRepository) {
        this.trackingGoalQueryService = trackingGoalQueryService;
        this.macronutrientValuesCommandService = macronutrientValuesCommandService;
        this.trackingGoalCommandService = trackingGoalCommandService;
        this.macronutrientValuesRepository = macronutrientValuesRepository;
    }

    @Operation(
            summary = "Get all available goal types",
            description = "Fetch all available goal types with their macronutrient values",
            operationId = "getGoalTypes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalTypeResource.class)
                            )
                    )
            }
    )
    @GetMapping("/goal-types")
    public ResponseEntity<List<GoalTypeResource>> getGoalTypes() {
        List<GoalTypeResource> goalTypes = Arrays.stream(GoalTypes.values())
                .map(GoalTypeResource::from)
                .toList();
        return ResponseEntity.ok(goalTypes);
    }

    @Operation(
            summary = "Update tracking goal by user ID",
            description = "Update the tracking goal and macronutrient targets for a specific user",
            operationId = "updateTrackingGoalByUserId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tracking goal updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tracking goal not found for user"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    @PutMapping("/user/{userId}")
    public ResponseEntity<Void> updateTrackingGoalByUserId(
            @PathVariable Long userId,
            @RequestBody UpdateTrackingGoalResource resource) {

        try {
            var updateCommand = new UpdateTrackingGoalCommand(
                    new UserId(userId),
                    resource.goalType()
            );

            trackingGoalCommandService.handle(updateCommand);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get tracking goal by user ID",
            description = "Fetch tracking goal information for a specific user",
            operationId = "getTrackingGoalByUserId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrackingGoalResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tracking goal not found"
                    )
            }
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<TrackingGoalResource> getTrackingGoalByUserId(@PathVariable Long userId) {
        var getTrackingGoalByUserIdQuery = new GetTrackingGoalByUserIdQuery(new UserId(userId));
        var optionalTrackingGoal = this.trackingGoalQueryService.handle(getTrackingGoalByUserIdQuery);

        if (optionalTrackingGoal.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var trackingGoalResource = TrackingGoalResourceFromEntityAssembler.toResource(optionalTrackingGoal.get());
        return ResponseEntity.ok(trackingGoalResource);
    }

    @Operation(
            summary = "Get target macronutrients by tracking goal ID",
            description = "Fetch target macronutrients for a specific tracking goal",
            operationId = "getTargetMacronutrientsByTrackingGoalId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MacronutrientValuesResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Target macronutrients not found"
                    )
            }
    )
    @GetMapping("/{trackingGoalId}/target-macros")
    public ResponseEntity<MacronutrientValuesResource> getTargetMacronutrientsByTrackingGoalId(@PathVariable Long trackingGoalId) {
        var getTargetMacronutrientsQuery = new GetTargetMacronutrientsQuery(trackingGoalId);
        var optionalMacros = this.trackingGoalQueryService.handle(getTargetMacronutrientsQuery);

        if (optionalMacros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var macronutrientsResource = MacronutrientValuesResourceFromEntityAssembler.toResource(optionalMacros.get());
        return ResponseEntity.ok(macronutrientsResource);
    }

    @Operation(
            summary = "Create a new Tracking Goal",
            description = "Creates a new TrackingGoal with target macronutrient values",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tracking goal created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )

    @PostMapping
    public ResponseEntity<Long> createTrackingGoal(@RequestBody CreateTrackingGoalResource resource) {
        // Transformar el Resource a Command usando el Assembler
        var command = CreateTrackingGoalCommandFromResourceAssembler.toCommand(resource);

        // Ejecutar la lógica del dominio
        Long goalId = trackingGoalCommandService.handle(command);

        // Devolver el ID del TrackingGoal creado
        return ResponseEntity.status(201).body(goalId);
    }

    @Operation(
            summary = "Create tracking goal from profile objective",
            description = "Creates a tracking goal automatically based on the user's profile objective",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tracking goal created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid profile or objective"),
                    @ApiResponse(responseCode = "409", description = "Tracking goal already exists for user")
            }
    )
    @PostMapping("/from-profile/{profileId}")
    public ResponseEntity<Long> createTrackingGoalFromProfile(@PathVariable Long profileId) {
        try {
            Long goalId = trackingGoalCommandService.createTrackingGoalFromProfile(profileId);
            return ResponseEntity.status(201).body(goalId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(409).build(); // Conflict - goal already exists
        }
    }

    @Operation(
            summary = "Update tracking goal from profile objective",
            description = "Updates an existing tracking goal based on the current user's profile objective",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tracking goal updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid profile or objective"),
                    @ApiResponse(responseCode = "404", description = "Tracking goal not found for user")
            }
    )
    @PutMapping("/from-profile/{profileId}")
    public ResponseEntity<Void> updateTrackingGoalFromProfile(@PathVariable Long profileId) {
        try {
            trackingGoalCommandService.updateTrackingGoalFromProfile(profileId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Check if tracking goal exists for profile",
            description = "Checks if a tracking goal exists for the given profile ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check completed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid profile ID")
            }
    )
    @GetMapping("/exists/profile/{profileId}")
    public ResponseEntity<Boolean> existsTrackingGoalForProfile(@PathVariable Long profileId) {
        try {
            boolean exists = trackingGoalCommandService.existsTrackingGoalForProfile(profileId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}