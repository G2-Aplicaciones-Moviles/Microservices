package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.recipes.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.center.jameoFit.recipes.interfaces.rest.resources.CreateCategoryResource;

public class CreateCategoryCommandFromResourceAssembler {

    public static CreateCategoryCommand toCommandFromResource(CreateCategoryResource resource) {
        return new CreateCategoryCommand(resource.name());
    }
}
