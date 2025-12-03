package pe.edu.upc.recipes_service.recipes.interfaces.rest.transform;

import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.RecipeNutrition;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.RecipeNutritionResource;

public class RecipeNutritionResourceAssembler {
    public static RecipeNutritionResource toResource(RecipeNutrition vo) {
        return new RecipeNutritionResource(vo.calories(), vo.carbs(), vo.proteins(), vo.fats());
    }
}