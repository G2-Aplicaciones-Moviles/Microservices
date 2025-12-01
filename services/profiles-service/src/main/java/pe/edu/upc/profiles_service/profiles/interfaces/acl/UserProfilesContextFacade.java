package pe.edu.upc.profiles_service.profiles.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.profiles.domain.model.Entities.Objective;
import pe.edu.upc.center.jameoFit.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.center.jameoFit.profiles.domain.model.commands.CreateUserProfileCommand;
import pe.edu.upc.center.jameoFit.profiles.domain.model.commands.DeleteUserProfileCommand;
import pe.edu.upc.center.jameoFit.profiles.domain.model.commands.UpdateUserProfileCommand;
import pe.edu.upc.center.jameoFit.profiles.domain.model.queries.GetAllUserProfilesQuery;
import pe.edu.upc.center.jameoFit.profiles.domain.model.queries.GetUserProfileByIdQuery;
import pe.edu.upc.center.jameoFit.profiles.domain.services.UserProfileCommandService;
import pe.edu.upc.center.jameoFit.profiles.domain.services.UserProfileQueryService;
import pe.edu.upc.center.jameoFit.profiles.interfaces.rest.resources.CreateUserProfileResource;
import pe.edu.upc.center.jameoFit.profiles.interfaces.rest.resources.UserProfileResource;
import pe.edu.upc.center.jameoFit.profiles.interfaces.rest.transform.CreateUserProfileCommandFromResourceAssembler;
import pe.edu.upc.center.jameoFit.profiles.interfaces.rest.transform.UpdateUserProfileCommandFromResourceAssembler;
import pe.edu.upc.center.jameoFit.profiles.interfaces.rest.transform.UserProfileResourceFromEntityAssembler;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfilesContextFacade {

    private final UserProfileCommandService commandService;
    private final UserProfileQueryService queryService;

    public UserProfilesContextFacade(UserProfileCommandService commandService,
                                     UserProfileQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    public List<UserProfileResource> fetchAll() {
        var entities = queryService.handle(new GetAllUserProfilesQuery());
        return UserProfileResourceFromEntityAssembler.toResources(entities);
    }

    public Optional<UserProfileResource> fetchById(Long id) {
        return queryService.handle(new GetUserProfileByIdQuery(id))
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity);
    }

    /**
     * Obtiene el objetivo de un perfil de usuario por su ID
     * @param profileId ID del perfil
     * @return Optional con el objetivo del perfil si existe
     */
    public Optional<Objective> fetchObjectiveByProfileId(Long profileId) {
        return queryService.handle(new GetUserProfileByIdQuery(profileId))
                .map(UserProfile::getObjective);
    }

    /**
     * Obtiene el nombre del objetivo de un perfil de usuario
     * @param profileId ID del perfil
     * @return Optional con el nombre del objetivo si existe
     */
    public Optional<String> fetchObjectiveNameByProfileId(Long profileId) {
        return fetchObjectiveByProfileId(profileId)
                .map(Objective::getObjectiveName);
    }

    /**
     * Verifica si un perfil existe
     * @param profileId ID del perfil
     * @return true si el perfil existe, false en caso contrario
     */
    public boolean existsProfileById(Long profileId) {
        return queryService.handle(new GetUserProfileByIdQuery(profileId)).isPresent();
    }

    /**
     * Obtiene el UserProfile completo por ID (para uso interno del facade)
     * @param profileId ID del perfil
     * @return Optional con el UserProfile si existe
     */
    public Optional<UserProfile> fetchUserProfileById(Long profileId) {
        return queryService.handle(new GetUserProfileByIdQuery(profileId));
    }

    public int create(CreateUserProfileResource resource) {
        CreateUserProfileCommand cmd = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        return commandService.handle(cmd);
    }

    public void update(Long id, CreateUserProfileResource resource) {
        UpdateUserProfileCommand cmd = UpdateUserProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(cmd);
    }

    public void delete(Long id) {
        commandService.handle(new DeleteUserProfileCommand(id));
    }
}