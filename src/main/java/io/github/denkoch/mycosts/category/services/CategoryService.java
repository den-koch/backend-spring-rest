package io.github.denkoch.mycosts.category.services;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Collection<Category> readCategories(Long userId) {
        return categoryRepository.findAll(userId);
    }

    public Category readCategory(Long userId, Long id) {
        return categoryRepository.findById(userId, id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long userId, Long id) {
        categoryRepository.deleteById(userId, id);
    }

}
