package io.github.denkoch.mycosts.category.services;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.exceptions.ResourceAlreadyExistsException;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Collection<Category> readCategories(User user) {
        return categoryRepository.findAllByUser(user);
    }

    public Optional<Category> readCategory(User user, UUID id) {
        return categoryRepository.findByUserAndId(user, id);
    }

    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.findByUserAndLabel(category.getUser(), category.getLabel()).isPresent()) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(UUID userId, UUID id) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User {" + "} not found"));

        categoryRepository.deleteByUserAndId(user, id);
    }

}
