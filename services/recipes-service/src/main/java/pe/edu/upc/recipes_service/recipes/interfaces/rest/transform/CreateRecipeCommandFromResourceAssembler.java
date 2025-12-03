package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeCommand;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateRecipeResource;

public class CreateRecipeCommandFromResourceAssembler {

    /**
     * Construye CreateRecipeCommand a partir del recurso y la metadata de asignación/creación.
     * @param resource               El recurso con los detalles de la receta (nombre, tiempo, etc.).
     * @param createdByNutritionistId El ID del nutricionista que la creó (null si la creó un usuario).
     * @param assignedToProfileId     El ID del perfil al que se asigna (null si es una plantilla).
     * @return CreateRecipeCommand
     */
    public static CreateRecipeCommand toCommandFromResource(
            CreateRecipeResource resource,
            Long createdByNutritionistId,
            Integer assignedToProfileId) {

        return new CreateRecipeCommand(
                resource.name(),
                resource.description(),
                resource.preparationTime(),
                resource.difficulty(),
                resource.categoryId(),
                resource.recipeTypeId(),
                createdByNutritionistId,
                assignedToProfileId
        );
    }
}