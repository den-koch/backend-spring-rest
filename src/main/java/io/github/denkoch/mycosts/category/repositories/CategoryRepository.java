package io.github.denkoch.mycosts.category.repositories;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.payment.models.Payment;

import java.util.Collection;
import java.util.UUID;

public interface CategoryRepository {
    Category findById(UUID userId, UUID id);
    Collection<Category> findAll(UUID userId);

    Category save(Category category);

    void deleteById(UUID userId, UUID id);

    Category findByLabel(UUID userId, String label);
}
