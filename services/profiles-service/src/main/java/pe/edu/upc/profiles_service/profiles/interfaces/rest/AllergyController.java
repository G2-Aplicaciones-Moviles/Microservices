package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profiles_service.profiles.application.internal.commandservices.AllergyCommandService;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Allergy;

import java.util.List;

@RestController
@RequestMapping("/api/v1/allergies")
@Tag(name = "Allergies", description = "Gestión de alergias")
public class AllergyController {

    private final AllergyCommandService allergyCommandService;

    public AllergyController(AllergyCommandService allergyCommandService) {
        this.allergyCommandService = allergyCommandService;
    }

    @GetMapping
    public ResponseEntity<List<Allergy>> getAll() {
        var allergies = allergyCommandService.getAllAllergies();
        return ResponseEntity.ok(allergies);
    }

    @PostMapping
    public ResponseEntity<Allergy> create(@RequestBody Allergy allergy) {
        try {
            var createdAllergy = allergyCommandService.createAllergy(allergy);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAllergy);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            allergyCommandService.deleteAllergy(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
