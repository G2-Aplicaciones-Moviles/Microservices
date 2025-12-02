package pe.edu.upc.mealplan_service.mealplan.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.ExternalProfileAndNutritionistService;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanEntryCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.DeleteMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.*;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserId;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanCommandService;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanEntryCommandService;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanQueryService;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.*;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.CreateMealPlanCommandFromResourceAssembler;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.CreateMealPlanEntryCommandFromResourceAssembler;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.transform.MealPlanResourceFromEntityAssembler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/meal-plan", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Meal Plan", description = "Meal Plans Management Endpoints")
public class MealPlanController {

    private final MealPlanCommandService mealPlanCommandService;
    private final MealPlanQueryService mealPlanQueryService;
    private final MealPlanEntryCommandService mealPlanEntryCommandService;
    private final ExternalProfileAndNutritionistService externalService;

    public MealPlanController(
            MealPlanQueryService mealPlanQueryService,
            MealPlanCommandService mealPlanCommandService,
            MealPlanEntryCommandService mealPlanEntryCommandService,
            ExternalProfileAndNutritionistService externalService) {

        this.mealPlanQueryService = mealPlanQueryService;
        this.mealPlanCommandService = mealPlanCommandService;
        this.mealPlanEntryCommandService = mealPlanEntryCommandService;
        this.externalService = externalService;
    }

    // ------------------------------------------------------------
    // 1) USER CREATES MEALPLAN FOR THEMSELVES
    // ------------------------------------------------------------
    @PostMapping("/users/{userId}")
    @Operation(summary = "Create meal plan for user")
    public ResponseEntity<MealPlanResource> createMealPlanForUser(
            @PathVariable Long userId,
            @RequestBody CreateMealPlanResource resource) {

        externalService.validateUserProfile(new UserId(userId).userId().intValue());

        if (resource.profileId() == null || !Objects.equals(resource.profileId().longValue(), userId)) {
            return ResponseEntity.badRequest().body(null);
        }

        CreateMealPlanCommand command =
                CreateMealPlanCommandFromResourceAssembler.toCommandFromResource(resource, (Long) null);

        var createdOpt = mealPlanCommandService.handle(command);
        if (createdOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var mpOpt = mealPlanQueryService.handle(new GetMealPlanByIdQuery(Math.toIntExact(createdOpt.get().getId())));
        return mpOpt.map(mealPlan -> new ResponseEntity<>(
                MealPlanResourceFromEntityAssembler.toResourceFromEntity(mealPlan),
                HttpStatus.CREATED
        )).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // ------------------------------------------------------------
    // 2) NUTRICIONISTA CREA TEMPLATE
    // ------------------------------------------------------------
    @PostMapping("/nutritionists/{nutritionistUserId}")
    @Operation(summary = "Nutritionist creates meal plan template")
    public ResponseEntity<MealPlanResource> createMealPlanForNutritionist(
            @PathVariable Long nutritionistUserId,
            @RequestBody CreateMealPlanResource resource) {

        externalService.validateNutritionist(new UserId(nutritionistUserId).userId().intValue());

        if (resource.profileId() != null && resource.profileId() > 0) {
            externalService.validateUserProfile(new UserId(resource.profileId().longValue()).userId().intValue());
        }

        CreateMealPlanCommand command =
                CreateMealPlanCommandFromResourceAssembler.toCommandFromResource(resource, nutritionistUserId);

        var createdOpt = mealPlanCommandService.handle(command);
        if (createdOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var mpOpt = mealPlanQueryService.handle(new GetMealPlanByIdQuery(Math.toIntExact(createdOpt.get().getId())));
        if (mpOpt.isEmpty()) return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(
                MealPlanResourceFromEntityAssembler.toResourceFromEntity(mpOpt.get()),
                HttpStatus.CREATED
        );
    }

    // ------------------------------------------------------------
    // 3) LIST ORIGINAL TEMPLATES BY NUTRITIONIST
    // ------------------------------------------------------------
    @GetMapping("/nutritionists/{nutritionistUserId}")
    @Operation(summary = "List ORIGINAL templates created by nutritionist")
    public ResponseEntity<List<MealPlanResource>> getMealPlansByNutritionist(
            @PathVariable Long nutritionistUserId) {

        externalService.validateNutritionist(new UserId(nutritionistUserId).userId().intValue());

        var mealPlans = mealPlanQueryService.handle(new GetAllMealPlanQuery())
                .stream()
                .filter(mp ->
                        mp.getCreatedByNutritionistId() != null &&
                                Objects.equals(mp.getCreatedByNutritionistId().longValue(), nutritionistUserId) &&
                                mp.getProfileId() == null
                )
                .map(MealPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(mealPlans);
    }

    // ------------------------------------------------------------
    // 4) ASSIGN / COPY TEMPLATE TO PROFILE
    // ------------------------------------------------------------
    @PostMapping("/{mealPlanId}/assign-to-profile/{profileId}")
    @Operation(summary = "Assign/copy template to user profile")
    public ResponseEntity<?> assignMealPlanToProfile(
            @PathVariable Long mealPlanId,
            @PathVariable Long profileId) {

        externalService.validateUserProfile(new UserId(profileId).userId().intValue());

        var existingOpt = mealPlanQueryService.handle(new GetMealPlanByIdQuery(mealPlanId.intValue()));
        if (existingOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "MealPlan not found"));

        MealPlan existing = existingOpt.get();

        if (existing.getProfileId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Cannot assign. MealPlan is not a template."));
        }

        Float calories = existing.getMacros() != null ? (float) existing.getMacros().getCalories() : null;
        Float carbs = existing.getMacros() != null ? (float) existing.getMacros().getCarbs() : null;
        Float proteins = existing.getMacros() != null ? (float) existing.getMacros().getProteins() : null;
        Float fats = existing.getMacros() != null ? (float) existing.getMacros().getFats() : null;

        Integer targetProfileId = profileId == null ? null : profileId.intValue();

        CreateMealPlanResource copyResource = new CreateMealPlanResource(
                existing.getName(),
                existing.getDescription(),
                calories,
                carbs,
                proteins,
                fats,
                targetProfileId,
                existing.getCategory(),
                true,
                existing.getTags() != null ?
                        existing.getTags().getMealPlanTags().stream().map(t -> t.getTag()).toList() :
                        List.of(),
                null
        );

        Long createdByNutritionistIdLong =
                existing.getCreatedByNutritionistId() != null ?
                        existing.getCreatedByNutritionistId().longValue() : null;

        CreateMealPlanCommand copyCommand =
                CreateMealPlanCommandFromResourceAssembler.toCommandFromResource(copyResource, createdByNutritionistIdLong);

        var createdOpt = mealPlanCommandService.handle(copyCommand);
        if (createdOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var newEntityOpt = mealPlanQueryService.handle(new GetMealPlanByIdQuery(Math.toIntExact(createdOpt.get().getId())));
        if (newEntityOpt.isEmpty()) return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(
                MealPlanResourceFromEntityAssembler.toResourceFromEntity(newEntityOpt.get()),
                HttpStatus.CREATED
        );
    }

    // ------------------------------------------------------------
    // 5) ADD RECIPE TO MEAL PLAN + UPDATE TRACKING
    // ------------------------------------------------------------
    @PostMapping("/{mealPlanId}/entries")
    @Operation(summary = "Add a recipe to a MealPlan")
    public ResponseEntity<?> addRecipeToMealPlan(
            @PathVariable int mealPlanId,
            @RequestBody CreateMealPlanEntryResource body) {

        try {
            // Map resource to command
            CreateMealPlanEntryCommand cmd =
                    CreateMealPlanEntryCommandFromResourceAssembler.toCommandFromResource(mealPlanId, body);

            // Add entry
            Integer entryId = mealPlanEntryCommandService.handle(cmd);
            if (entryId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Could not add recipe to meal plan"));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "entryId", entryId,
                            "message", "Recipe added to meal plan"
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene TODOS los meal plan templates creados por nutricionistas
     * (sin importar qué nutricionista los creó)
     */
    @GetMapping("/templates")
    @Operation(summary = "Get all meal plan templates created by nutritionists")
    public ResponseEntity<List<MealPlanResource>> getAllTemplates() {
        var templates = mealPlanQueryService.handle(new GetAllMealPlanQuery())
                .stream()
                .filter(mp ->
                        // Es template si NO tiene profileId (no está asignado a usuario)
                        mp.getProfileId() == null &&
                                // Y fue creado por un nutricionista
                                mp.getCreatedByNutritionistId() != null
                )
                .map(MealPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(templates);
    }

    /**
     * Obtiene templates creados por nutricionistas con información adicional
     * (nombre del nutricionista, etc.)
     */
    @GetMapping("/templates/detailed")
    @Operation(summary = "Get all templates with nutritionist info")
    public ResponseEntity<List<MealPlanTemplateResource>> getAllTemplatesDetailed() {
        var templates = mealPlanQueryService.handle(new GetAllMealPlanQuery())
                .stream()
                .filter(mp ->
                        mp.getProfileId() == null &&
                                mp.getCreatedByNutritionistId() != null
                )
                .map(mp -> {
                    // Obtener info del nutricionista vía ACL
                    String nutritionistName = "Unknown";
                    try {
                        // TODO: Aquí deberías llamar a tu servicio externo para obtener el nombre
                        // var nutritionist = externalService.getNutritionistById(mp.getCreatedByNutritionistId());
                        // nutritionistName = nutritionist.getName();
                        nutritionistName = "Nutritionist #" + mp.getCreatedByNutritionistId();
                    } catch (Exception e) {
                        // Si falla, usar valor por defecto
                    }

                    return new MealPlanTemplateResource(
                            Math.toIntExact(mp.getId()),
                            mp.getName(),
                            mp.getDescription(),
                            mp.getCategory(),
                            mp.getCreatedByNutritionistId(),
                            nutritionistName,
                            mp.getMacros() != null ? mp.getMacros().getCalories() : 0,
                            mp.getMacros() != null ? mp.getMacros().getCarbs() : 0,
                            mp.getMacros() != null ? mp.getMacros().getProteins() : 0,
                            mp.getMacros() != null ? mp.getMacros().getFats() : 0
                    );
                })
                .toList();

        return ResponseEntity.ok(templates);
    }

    // ------------------------------------------------------------
    // READ ENDPOINTS
    // ------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<MealPlanResource>> getAllMealPlans() {
        var mealPlans = mealPlanQueryService.handle(new GetAllMealPlanQuery());
        return ResponseEntity.ok(
                mealPlans.stream()
                        .map(MealPlanResourceFromEntityAssembler::toResourceFromEntity)
                        .toList()
        );
    }

    @GetMapping("/detailed/{mealPlanId}")
    public List<?> getEntriesWithRecipeInfo(@PathVariable int mealPlanId) {
        return mealPlanQueryService.handle(new GetEntriesWithRecipeInfo(mealPlanId));
    }

    @GetMapping("/{mealPlanId}")
    public ResponseEntity<MealPlanResource> getMealPlanById(@PathVariable int mealPlanId) {
        var mp = mealPlanQueryService.handle(new GetMealPlanByIdQuery(mealPlanId));
        if (mp.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(MealPlanResourceFromEntityAssembler.toResourceFromEntity(mp.get()));
    }

    @DeleteMapping("/{mealPlanId}")
    public ResponseEntity<?> deleteMealPlan(@PathVariable int mealPlanId) {
        mealPlanCommandService.handle(new DeleteMealPlanCommand(mealPlanId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<MealPlanResource>> getMealPlansByProfileId(@PathVariable int profileId) {
        var mealPlans = mealPlanQueryService.handle(new GetAllMealPlanByProfileIdQuery(profileId));

        if (mealPlans.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        var resources = mealPlans.stream()
                .map(MealPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
