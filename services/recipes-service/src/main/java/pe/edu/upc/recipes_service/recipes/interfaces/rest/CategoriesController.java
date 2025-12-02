package pe.edu.upc.recipes_service.recipes.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices.CategoryCommandServiceImpl;
import pe.edu.upc.recipes_service.recipes.aplication.internal.queryservices.CategoryQueryServiceImpl;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.recipes_service.recipes.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CategoryResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.resources.CreateCategoryResource;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CategoryResourceFromEntityAssembler;
import pe.edu.upc.recipes_service.recipes.interfaces.rest.transform.CreateCategoryCommandFromResourceAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories")
@Tag(name = "Categories", description = "Category Management Endpoints")
public class CategoriesController {

    private final CategoryCommandServiceImpl categoryCommandService;
    private final CategoryQueryServiceImpl categoryQueryService;

    public CategoriesController(CategoryCommandServiceImpl categoryCommandService, CategoryQueryServiceImpl categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResource> createCategory(@RequestBody CreateCategoryResource resource) {
        var createCategoryCommand = CreateCategoryCommandFromResourceAssembler.toCommandFromResource(resource);
        var categoryId = this.categoryCommandService.handle(createCategoryCommand);

        var category = this.categoryQueryService.handle(new GetCategoryByIdQuery(categoryId));

        if (category.isEmpty())
            return ResponseEntity.notFound().build();

        var categoryResource = CategoryResourceFromEntityAssembler.toResourceFromEntity(category.get());
        return new ResponseEntity<>(categoryResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        var categories = this.categoryQueryService.handle(new GetAllCategoriesQuery());
        var categoryResources = CategoryResourceFromEntityAssembler.toResources(categories);
        return ResponseEntity.ok(categoryResources);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResource> getCategoryById(@PathVariable Long categoryId) {
        var optionalCategory = this.categoryQueryService.handle(new GetCategoryByIdQuery(categoryId));

        if (optionalCategory.isEmpty())
            return ResponseEntity.notFound().build();

        var categoryResource = CategoryResourceFromEntityAssembler.toResourceFromEntity(optionalCategory.get());
        return ResponseEntity.ok(categoryResource);
    }
}
