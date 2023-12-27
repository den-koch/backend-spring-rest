package io.github.denkoch.mycosts.category.repositories;

import io.github.denkoch.mycosts.category.models.Category;

import java.util.Collection;

public interface CategoryRepository {
    Category findById(Long userId, Long id);
    Collection<Category> findAll(Long userId);

    Category save(Category category);

    void deleteById(Long userId, Long id);

    Integer findByLabel(Long userId, String label);

    void deleteByUserId(Long id);
}
