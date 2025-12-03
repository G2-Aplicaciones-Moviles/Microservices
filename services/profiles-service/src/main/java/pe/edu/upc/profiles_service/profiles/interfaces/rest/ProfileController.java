package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profiles_service.profiles.domain.services.ProfileCommandService;
import pe.edu.upc.profiles_service.profiles.domain.services.ProfileQueryService;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.ProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.transform.UserResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "Registro de usuarios")
public class ProfileController {

    private final ProfileCommandService commandService;
    private final ProfileQueryService queryService;

    public ProfileController(ProfileCommandService commandService,
                             ProfileQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileResource>> listAll() {
        var profiles = queryService.getAllUsers();
        var resources = UserResourceFromEntityAssembler.toResources(profiles);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResource> getById(@PathVariable Long id) {
        return queryService.getUserById(id)
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody CreateProfileResource resource) {
        var command = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = commandService.handle(command);
        return ResponseEntity.ok(profile.getId().intValue());
    }
}
