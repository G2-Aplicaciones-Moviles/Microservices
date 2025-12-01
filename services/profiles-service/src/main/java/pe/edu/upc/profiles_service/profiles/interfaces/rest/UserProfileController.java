package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.profiles_service.profiles.interfaces.acl.UserProfilesContextFacade;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateUserProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.UserProfileResource;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-profiles")
@Tag(name = "UserProfiles", description = "Gestión del perfil del usuario (onboarding)")
public class UserProfileController {

    private final UserProfilesContextFacade facade;

    public UserProfileController(UserProfilesContextFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<UserProfileResource> listAll() {
        return facade.fetchAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResource> getById(@PathVariable Long id) {
        return facade.fetchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfileResource> create(@RequestBody CreateUserProfileResource r) {
        var newId = facade.create(r);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newId).toUri();

        var resource = facade.fetchById((long) newId).orElseThrow();
        return ResponseEntity.created(location).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CreateUserProfileResource r) {
        if (facade.fetchById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        facade.update(id, r);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
