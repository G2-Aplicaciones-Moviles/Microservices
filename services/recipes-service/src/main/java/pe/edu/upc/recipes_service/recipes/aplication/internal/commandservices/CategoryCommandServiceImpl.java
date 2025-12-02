package pe.edu.upc.recipes_service.recipes.aplication.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.recipes_service.recipes.domain.model.entities.Category;
import pe.edu.upc.recipes_service.recipes.domain.services.CategoryCommandService;
import pe.edu.upc.recipes_service.recipes.infrastructure.persistence.jpa.repositories.CategoryRepository;

@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public CategoryCommandServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long handle(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Category already exists with name: " + command.name());
        }

        var category = new Category(command.name());
        categoryRepository.save(category);
        return category.getId();
    }
}
