package pe.edu.upc.goals_service.goals.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.domain.model.queries.GetGoalByUserQuery;
import pe.edu.upc.goals_service.goals.domain.services.GoalCommandService;
import pe.edu.upc.goals_service.goals.domain.services.GoalQueryService;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.DietTypeConfigResource;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.GoalCalorieConfigResource;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.GoalResource;
import pe.edu.upc.goals_service.goals.interfaces.rest.transform.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/goals", produces = "application/json")
@Tag(name = "Goals", description = "Goals Management Endpoints")
public class GoalsController {

    private final GoalCommandService commandService;
    private final GoalQueryService queryService;

    public GoalsController(GoalCommandService commandService,
                           GoalQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PutMapping("/calories")
    public ResponseEntity<GoalResource> updateGoalCalories(
            @RequestParam Long userId,
            @RequestBody GoalCalorieConfigResource resource) {
        try {
            Goal updated = commandService.handle(
                    UpdateGoalCaloriesCommandFromResourceAssembler.toCommand(userId, resource)
            );
            var res = GoalResourceFromEntityAssembler.toResourceFromEntity(updated);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/diet-type")
    public ResponseEntity<GoalResource> updateDietType(
            @RequestParam Long userId,
            @RequestBody DietTypeConfigResource resource) {
        try {
            Goal updated = commandService.handle(
                    UpdateDietTypeCommandFromResourceAssembler.toCommand(userId, resource)
            );
            var res = GoalResourceFromEntityAssembler.toResourceFromEntity(updated);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<GoalResource> getByUser(@RequestParam Long userId) {
        try {
            Optional<Goal> goal = queryService.handle(new GetGoalByUserQuery(userId));
            return goal
                    .map(GoalResourceFromEntityAssembler::toResourceFromEntity)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}