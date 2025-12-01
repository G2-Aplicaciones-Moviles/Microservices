package pe.edu.upc.recipes_service.recipes.interfaces.rest.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteIngredientCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllIngredientsQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetIngredientsByIdQuery;
import pe.edu.upc.recipes_service.recipes.domain.services.IngredientCommandService;
import pe.edu.upc.recipes_service.recipes.domain.services.IngredientQueryService;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateIngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.IngredientResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CreateIngredientCommandFromResourceAssembler;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.IngredientResourceFromEntityAssembler;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientContextFacade {

    private final IngredientCommandService commandService;
    private final IngredientQueryService queryService;

    public IngredientContextFacade(IngredientCommandService commandService,
                                   IngredientQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    public List<IngredientResource> fetchAll() {
        var entities = queryService.handle(new GetAllIngredientsQuery());
        return IngredientResourceFromEntityAssembler.toResources(entities);
    }

    public Optional<IngredientResource> fetchById(int id) {
        return queryService.handle(new GetIngredientsByIdQuery(id))
                .map(IngredientResourceFromEntityAssembler::toResourceFromEntity);
    }

    public int create(CreateIngredientResource resource) {
        var cmd = CreateIngredientCommandFromResourceAssembler.toCommandFromResource(resource);
        return commandService.handle(cmd);
    }

    public void delete(int id) {
        commandService.handle(new DeleteIngredientCommand(id));
    }

    public boolean existsByName(String name) {
        return queryService.handle(new GetAllIngredientsQuery())
                .stream()
                .anyMatch(ing -> ing.getName().equalsIgnoreCase(name));
    }


}
