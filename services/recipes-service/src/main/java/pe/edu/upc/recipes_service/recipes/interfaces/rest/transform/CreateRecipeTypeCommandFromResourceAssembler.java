package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeTypeCommand;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateRecipeTypeResource;

public class CreateRecipeTypeCommandFromResourceAssembler {

    public static CreateRecipeTypeCommand toCommandFromResource(CreateRecipeTypeResource resource) {
        return new CreateRecipeTypeCommand(resource.name());
    }
}
