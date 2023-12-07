package io.github.denkoch.mycosts.category.repositories;

import io.github.denkoch.mycosts.category.models.Category;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private Map<UUID, Category> repository = new TreeMap<>();

    @Override
    public Category findById(UUID userId, UUID id) {
        return repository.values().stream()
                .filter(category -> userId.equals(category.getUserId()) && id.equals(category.getId())).findAny().orElse(null);
    }

    @Override
    public Collection<Category> findAll(UUID userId) {
        return repository.values().stream().filter(category -> userId.equals(category.getUserId())).toList();
    }

    public Category findByLabel(UUID userId, String label) {
        return repository.values().stream()
                .filter(category -> userId.equals(category.getUserId()) && label.equals(category.getLabel())).findAny().orElse(null);
    }

    @Override
    public Category save(Category category) {
        repository.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteById(UUID userId, UUID id) {
        if (findById(userId, id) != null) {
            repository.remove(id);
        }
    }
}
