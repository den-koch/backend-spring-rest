package io.github.denkoch.mycosts.category.repositories;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.user.models.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends ListCrudRepository<Category, UUID> {
    Collection<Category> findAllByUser(User user);

    Optional<Category> findByUserAndId(User user, UUID id);

    Optional<Category> findByUserAndLabel(User user, String label);

    void deleteByUserAndId(User user, UUID id);

    void deleteAllByUserId(UUID userId);
}
