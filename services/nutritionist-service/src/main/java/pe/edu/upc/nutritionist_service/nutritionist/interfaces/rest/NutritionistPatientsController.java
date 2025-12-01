package pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistPatientCommandService;
import pe.edu.upc.nutritionist_service.nutritionist.domain.services.NutritionistPatientQueryService;
import pe.edu.upc.nutritionist_service.nutritionist.domain.model.commands.ApprovePatientCommand;

import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.CreateNutritionistPatientResource;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.resources.NutritionistPatientResource;

import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform.CreateNutritionistPatientCommandFromResourceAssembler;
import pe.edu.upc.nutritionist_service.nutritionist.interfaces.rest.transform.NutritionistPatientResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/nutritionist-patients", produces = "application/json")
@Tag(name = "NutritionistPatients", description = "Relación Nutricionista–Paciente")
public class NutritionistPatientsController {

    private final NutritionistPatientCommandService commandService;
    private final NutritionistPatientQueryService queryService;

    public NutritionistPatientsController(
            NutritionistPatientCommandService commandService,
            NutritionistPatientQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<NutritionistPatientResource> create(@RequestBody CreateNutritionistPatientResource r) {
        var command = CreateNutritionistPatientCommandFromResourceAssembler.toCommandFromResource(r);
        var created = commandService.handle(command);
        return ResponseEntity.ok(NutritionistPatientResourceFromEntityAssembler.toResourceFromEntity(created));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        var updated = commandService.handle(new ApprovePatientCommand(id));
        return updated
                .map(NutritionistPatientResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nutritionist/{id}")
    public List<NutritionistPatientResource> getPatients(@PathVariable Integer id) {
        return queryService.findPatientsByNutritionist(id)
                .stream()
                .map(NutritionistPatientResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    @GetMapping("/patient/{id}")
    public List<NutritionistPatientResource> getNutritionists(@PathVariable Long id) {
        return queryService.findNutritionistsOfPatient(id)
                .stream()
                .map(NutritionistPatientResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}

