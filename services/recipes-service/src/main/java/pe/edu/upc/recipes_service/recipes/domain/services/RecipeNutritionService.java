package pe.edu.upc.recipes_service.recipes.domain.services;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.RecipeNutrition;

@Service
public class RecipeNutritionService {

    // Asume que los macros de Ingredient están expresados "por 100 g".
    public RecipeNutrition compute(Recipe recipe) {
        double kc = 0, c = 0, p = 0, f = 0;

        for (var ri : recipe.getRecipeIngredients()) {
            var ing = ri.getIngredient();
            double factor = ri.getAmountGrams() / 100.0;
            kc += ing.getCalories()      * factor;
            c  += ing.getCarbohydrates() * factor;
            p  += ing.getProteins()      * factor;
            f  += ing.getFats()          * factor;
        }
        return new RecipeNutrition(kc, c, p, f);
    }
}