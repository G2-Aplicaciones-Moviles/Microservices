package pe.edu.upc.recipes_service.recipes.domain.model.commands;

// recipes.domain.model.commands
public record AddIngredientToRecipeCommand(int recipeId, int ingredientId, double amountGrams) { }
