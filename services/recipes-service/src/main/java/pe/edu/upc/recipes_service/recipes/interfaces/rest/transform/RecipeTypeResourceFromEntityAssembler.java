package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.entities.RecipeType;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeTypeResource;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeTypeResourceFromEntityAssembler {

    public static RecipeTypeResource toResourceFromEntity(RecipeType recipeType) {
        return new RecipeTypeResource(recipeType.getId(), recipeType.getName());
    }

    public static List<RecipeTypeResource> toResources(List<RecipeType> recipeTypes) {
        return recipeTypes.stream()
                .map(RecipeTypeResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
