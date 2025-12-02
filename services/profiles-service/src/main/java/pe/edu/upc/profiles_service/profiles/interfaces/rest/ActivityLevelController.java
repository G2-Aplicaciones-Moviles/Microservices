package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profiles_service.profiles.domain.model.Entities.ActivityLevel;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.ActivityLevelRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-levels")
@Tag(name = "Activity-levels", description = "registras tu nivel de actividad física")

public class ActivityLevelController {
    private final ActivityLevelRepository repository;

    public ActivityLevelController(ActivityLevelRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ActivityLevel> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ActivityLevel create(@RequestBody ActivityLevel level) {
        level.setId(null);


        return repository.save(level);
    }
}
