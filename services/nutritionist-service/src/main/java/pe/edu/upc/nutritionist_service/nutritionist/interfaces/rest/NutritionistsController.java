package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.queries.GetNutritionistByUserIdQuery;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistCommandService;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistQueryService;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.CreateNutritionistResource;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.NutritionistResource;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.UpdateNutritionistResource;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/nutritionists", produces = "application/json")
@Tag(name = "Nutritionists", description = "Gestión de Nutricionistas")
public class NutritionistsController {

    private final NutritionistCommandService commandService;
    private final NutritionistQueryService queryService;

    public NutritionistsController(NutritionistCommandService commandService,
                                   NutritionistQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public List<NutritionistResource> getAll() {
        return queryService.handleAll().stream()
                .map(NutritionistResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    @GetMapping("/by-user")
    public ResponseEntity<NutritionistResource> getByUser(@RequestParam Long userId) {
        return queryService.handle(new GetNutritionistByUserIdQuery(userId))
                .map(NutritionistResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NutritionistResource> create(@RequestBody CreateNutritionistResource resource) {
        var created = commandService.handle(CreateNutritionistCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntity.ok(NutritionistResourceFromEntityAssembler.toResourceFromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionistResource> update(@PathVariable Long id,
                                                       @RequestBody UpdateNutritionistResource resource) {
        var updated = commandService.handle(UpdateNutritionistCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return updated
                .map(NutritionistResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
