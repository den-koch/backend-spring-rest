package io.github.denkoch.mycosts.category.services;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.exceptions.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Collection<Category> readCategories(UUID userId) {
        return categoryRepository.findAll(userId);
    }

    public Category readCategory(UUID userId, UUID id) {
        return categoryRepository.findById(userId, id);
    }

    public Category createCategory(Category category) {
        if (categoryRepository.findByLabel(category.getUserId(), category.getLabel()) != null) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }
        UUID id = UUID.randomUUID();
        category.setId(id);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID userId, UUID id) {
        categoryRepository.deleteById(userId, id);
    }

}
