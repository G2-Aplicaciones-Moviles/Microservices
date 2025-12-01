// ProfileController.java
package pe.edu.upc.profiles_service.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profiles_service.profiles.interfaces.acl.ProfileContextFacade;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.CreateProfileResource;
import pe.edu.upc.profiles_service.profiles.interfaces.rest.resources.ProfileResource;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "se registra el usuario")
public class ProfileController {
    private final ProfileContextFacade facade;

    public ProfileController(ProfileContextFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<ProfileResource> listAll() {
        return facade.fetchAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResource> getById(@PathVariable Long id) {
        return facade.fetchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody CreateProfileResource r) {
        var newId = facade.create(r);
        return ResponseEntity.ok(newId);
    }
}
