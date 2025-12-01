package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.Objective;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.ObjectiveRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/objectives")
@Tag(name = "Objetives", description = "registras tu objetivo")
public class ObjectiveController {
    private final ObjectiveRepository repository;

    public ObjectiveController(ObjectiveRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Objective> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Objective create(@RequestBody Objective obj) {
        obj.setId(0);
        return repository.save(obj);
    }
}
