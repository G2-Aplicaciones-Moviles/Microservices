package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.DeleteUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetAllUserProfilesQuery;
import pe.edu.upc.profiles_service.profiles.domain.model.queries.GetUserProfileByIdQuery;
import pe.edu.upc.profiles_service.profiles.domain.services.UserProfileCommandService;
import pe.edu.upc.profiles_service.profiles.domain.services.UserProfileQueryService;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateUserProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.UserProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.transform.CreateUserProfileCommandFromResourceAssembler;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.transform.UpdateUserProfileCommandFromResourceAssembler;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.transform.UserProfileResourceFromEntityAssembler;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-profiles")
@Tag(name = "UserProfiles", description = "Gestión del perfil del usuario (onboarding)")
public class UserProfileController {

    private final UserProfileCommandService commandService;
    private final UserProfileQueryService queryService;

    public UserProfileController(UserProfileCommandService commandService,
                                 UserProfileQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResource>> listAll() {
        var entities = queryService.handle(new GetAllUserProfilesQuery());
        var resources = UserProfileResourceFromEntityAssembler.toResources(entities);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetUserProfileByIdQuery(id))
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfileResource> getByUserId(@PathVariable Long userId) {
        var profiles = queryService.handle(new GetAllUserProfilesQuery());
        return profiles.stream()
                .filter(profile -> profile.getUserId().equals(userId))
                .findFirst()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfileResource> create(@RequestBody CreateUserProfileResource resource) {
        var command = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var newId = commandService.handle(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newId)
                .toUri();

        var createdProfile = queryService.handle(new GetUserProfileByIdQuery((long) newId))
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .orElseThrow();

        return ResponseEntity.created(location).body(createdProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody CreateUserProfileResource resource) {
        if (queryService.handle(new GetUserProfileByIdQuery(id)).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var command = UpdateUserProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (queryService.handle(new GetUserProfileByIdQuery(id)).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        commandService.handle(new DeleteUserProfileCommand(id));
        return ResponseEntity.noContent().build();
    }
}
