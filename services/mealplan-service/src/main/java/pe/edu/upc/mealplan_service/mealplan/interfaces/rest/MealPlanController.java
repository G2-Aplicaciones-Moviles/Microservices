package pe.edu.upc.mealplan_service.mealplan.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.DeleteMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetAllMealPlanQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetEntriesWithRecipeInfo;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.GetMealPlanByIdQuery;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanCommandService;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanEntryCommandService;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanQueryService;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.CreateMealPlanEntryResource;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.CreateMealPlanResource;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanResource;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.CreateMealPlanCommandFromResourceAssembler;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.CreateMealPlanEntryCommandFromResourceAssembler;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.MealPlanResourceFromEntityAssembler;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.UpdateMealPlanCommandFromResourceAssembler;
import pe.edu.upc.center.jameoFit.recipes.domain.model.queries.GetAllRecipesQuery;
import pe.edu.upc.center.jameoFit.recipes.interfaces.rest.resources.RecipeResource;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/meal-plan", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Meal Plan", description = "Meal plans Management Endpoints")
public class MealPlanController {
    private final MealPlanCommandService mealPlanCommandService;
    private final MealPlanQueryService mealPlanQueryService;
    private final MealPlanEntryCommandService mealPlanEntryCommandService;

    public MealPlanController(MealPlanQueryService mealPlanQueryService, MealPlanCommandService mealPlanCommandService
    , MealPlanEntryCommandService mealPlanEntryCommandService) {
        this.mealPlanQueryService = mealPlanQueryService;
        this.mealPlanCommandService = mealPlanCommandService;
        this.mealPlanEntryCommandService = mealPlanEntryCommandService;
    }

    @Operation(
            summary = "Add a new meal plan item",
            description = "Add a new meal plan",
            operationId = "createMealPlan",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MealPlanResource.class)
                            )
                    ),
                    @ApiResponse (
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content (
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RuntimeException.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<MealPlanResource> createMealPlan(@RequestBody CreateMealPlanResource resource) {
        // Create meal plan
        var createMealPlanCommand = CreateMealPlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var mealPlanEntity = this.mealPlanCommandService.handle(createMealPlanCommand);
        // Validate if meal plan is empty
        if (mealPlanEntity.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // Fetch meal plan
        var getMealPlanByIdQuery = new GetMealPlanByIdQuery(mealPlanEntity.get().getId());
        var mealplan = this.mealPlanQueryService.handle(getMealPlanByIdQuery);
        if (mealplan.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

//        var studentResource = this.externalProfileService.fetchStudentResourceFromProfileId(student.get()).get();
        return new ResponseEntity<>(MealPlanResourceFromEntityAssembler.toResourceFromEntity(mealplan.get()), HttpStatus.CREATED);

    }
    @GetMapping
    public ResponseEntity<List<MealPlanResource>> getAllMealPlans() {
        var getAllMealPlansQuery = new GetAllMealPlanQuery();
        var mealPlans = this.mealPlanQueryService.handle(
                getAllMealPlansQuery);
        return ResponseEntity.ok(
                mealPlans.stream()
                        .map(MealPlanResourceFromEntityAssembler::toResourceFromEntity)
                        .toList()
        );
    }
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResource>> getAllRecipes() {
        var getAllRecipesQuery = new GetAllRecipesQuery();
        var recipes = mealPlanQueryService.handle(getAllRecipesQuery);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/detailed/{mealPlanId}")
    public List<MealPlanEntryDetailedResource> getEntriesWithRecipeInfo(@PathVariable int mealPlanId) {
        var getEntriesWithRecipeInfoQuery = new GetEntriesWithRecipeInfo(mealPlanId);
        return mealPlanQueryService.handle(getEntriesWithRecipeInfoQuery);
    }

    @GetMapping("/{mealPlanId}")
    public ResponseEntity<MealPlanResource> getMealPlanById(@PathVariable int mealPlanId) {
        var getMealPlanByIdQuery = new GetMealPlanByIdQuery(mealPlanId);
        var mealPlanResource = this.mealPlanQueryService.handle(getMealPlanByIdQuery);
        if (mealPlanResource.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(MealPlanResourceFromEntityAssembler.toResourceFromEntity(mealPlanResource.get()));
    }

    @PutMapping("/{mealPlanId}")
    public ResponseEntity<MealPlanResource> updateMealPlan(@PathVariable int mealPlanId, @RequestBody MealPlanResource resource) {
        var updateMealPlanCommand = UpdateMealPlanCommandFromResourceAssembler.toCommandFromResource(resource, mealPlanId);
        var optionalMealPlan = this.mealPlanCommandService.handle(updateMealPlanCommand);
        if (optionalMealPlan.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(MealPlanResourceFromEntityAssembler.toResourceFromEntity(optionalMealPlan.get()));
    }

    @DeleteMapping("/{mealPlanId}")
    public ResponseEntity<?> deleteMealPlan(@PathVariable int mealPlanId) {
        var deleteMealPlanCommand = new DeleteMealPlanCommand(mealPlanId);
        this.mealPlanCommandService.handle(deleteMealPlanCommand);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{mealPlanId}/entries")
    public ResponseEntity<?> addEntry(@PathVariable int mealPlanId,
                                      @RequestBody CreateMealPlanEntryResource body) {
        var cmd = CreateMealPlanEntryCommandFromResourceAssembler.toCommandFromResource(mealPlanId, body);
        try {
            var entryId = mealPlanEntryCommandService.handle(cmd); // <- úsalo desde el campo
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(java.util.Map.of("entryId", entryId, "message", "Recipe added to meal plan"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

}
