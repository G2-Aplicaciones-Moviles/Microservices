package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.jameoFit.recipes.interfaces.rest.acl.RecipeContextFacade;
import pe.edu.upc.center.jameoFit.recipes.interfaces.rest.resources.RecipeNutritionResource;
import pe.edu.upc.center.jameoFit.recipes.interfaces.rest.resources.RecipeResource;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalMealPlanRecipeService {

    private final RecipeContextFacade recipeContextFacade;

    public ExternalMealPlanRecipeService(RecipeContextFacade recipeContextFacade) {
        this.recipeContextFacade = recipeContextFacade;
    }

    public Optional<RecipeResource> fetchRecipeById(int recipeId) {
        return recipeContextFacade.fetchById(recipeId);
    }
    public List<RecipeResource> fetchAllRecipes() {
        return recipeContextFacade.fetchAll();
    }
    public RecipeNutritionResource fetchNutrition(int recipeId) {
        return recipeContextFacade.fetchNutritionByRecipeId(recipeId);
    }
}
