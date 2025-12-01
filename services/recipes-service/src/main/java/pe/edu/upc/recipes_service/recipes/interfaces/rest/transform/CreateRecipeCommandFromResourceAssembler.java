package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeCommand;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateRecipeResource;

public class CreateRecipeCommandFromResourceAssembler {

    public static CreateRecipeCommand toCommandFromResource(CreateRecipeResource resource) {
        return new CreateRecipeCommand(
                resource.userId(),
                resource.name(),
                resource.description(),
                resource.preparationTime(),
                resource.difficulty(),
                resource.categoryId(),
                resource.recipeTypeId()
        );
    }
}
