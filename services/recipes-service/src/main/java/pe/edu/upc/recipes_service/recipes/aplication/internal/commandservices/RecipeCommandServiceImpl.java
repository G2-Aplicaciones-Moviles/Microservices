package pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.ExternalProfileAndTrackingService;
import pe.edu.upc.recipes_service.recipes.domain.model.aggregates.Recipe;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.AddIngredientToRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.DeleteRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.UpdateRecipeCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.valueobjects.UserId;
import pe.edu.upc.recipes_service.recipes.domain.services.RecipeCommandService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.IngredientRepository;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.RecipeRepository;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.RecipeTypeRepository;

import java.util.Optional;

@Service
public class RecipeCommandServiceImpl implements RecipeCommandService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeTypeRepository recipeTypeRepository;
    private final IngredientRepository ingredientRepository;
    private final ExternalProfileAndTrackingService externalProfileAndTrackingService;

    public RecipeCommandServiceImpl(RecipeRepository recipeRepository, CategoryRepository categoryRepository, RecipeTypeRepository recipeTypeRepository, IngredientRepository ingredientRepository, @Lazy ExternalProfileAndTrackingService externalProfileAndTrackingService) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.recipeTypeRepository = recipeTypeRepository;
        this.ingredientRepository = ingredientRepository;
        this.externalProfileAndTrackingService = externalProfileAndTrackingService;
    }

    @Override
    public int handle(CreateRecipeCommand command) {
        // Validar si ya existe una receta con el mismo nombre
        if (recipeRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("A recipe with the name " + command.name() + " already exists.");
        }

        // Validar existencia de UserId
        externalProfileAndTrackingService.validateUserExists(new UserId(command.userId()));

        var category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        var recipeType = recipeTypeRepository.findById(command.recipeTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Recipe type not found"));

        var recipe = new Recipe(
                command.userId(),
                command.name(),
                command.description(),
                command.preparationTime(),
                command.difficulty(),
                category,
                recipeType
        );

        try {
            this.recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving recipe: " + e.getMessage());
        }

        return recipe.getId();
    }

    @Override
    public Optional<Recipe> handle(UpdateRecipeCommand command) {
        var optionalRecipe = recipeRepository.findById(command.recipeId());
        if (optionalRecipe.isEmpty()) {
            throw new IllegalArgumentException("Recipe not found");
        }

        var recipe = optionalRecipe.get();

        // Validar si el nuevo nombre ya existe en otra receta
        if (!recipe.getName().equals(command.name()) && recipeRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Another recipe with the name " + command.name() + " already exists.");
        }

        var category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        var recipeType = recipeTypeRepository.findById(command.recipeTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Recipe type not found"));

        // Actualizar los datos
        recipe.updateRecipe(
                command.name(),
                command.description(),
                command.preparationTime(),
                command.difficulty(),
                category,
                recipeType
        );

        try {
            this.recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving recipe: " + e.getMessage());
        }
        return Optional.of(recipe);
    }

    @Override
    public void handle(DeleteRecipeCommand command) {
        if (!recipeRepository.existsById(command.recipeId())) {
            throw new IllegalArgumentException("Recipe not found");
        }
        try {
            this.recipeRepository.deleteById(command.recipeId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting recipe: " + e.getMessage());
        }
    }

    @Override
    public void handle(AddIngredientToRecipeCommand command) {
        if (command.amountGrams() <= 0)
            throw new IllegalArgumentException("amountGrams must be > 0");

        var optionalRecipe = recipeRepository.findById(command.recipeId());
        var optionalIngredient = ingredientRepository.findById(command.ingredientId());

        if (optionalRecipe.isEmpty() || optionalIngredient.isEmpty())
            throw new IllegalArgumentException("Recipe or Ingredient not found.");

        var recipe = optionalRecipe.get();
        var ingredient = optionalIngredient.get();

        // Usar helper del agregado para mantener la invariantes
        recipe.addIngredient(ingredient, command.amountGrams());

        recipeRepository.save(recipe); // Cascade ALL persistirá RecipeIngredient
    }
}