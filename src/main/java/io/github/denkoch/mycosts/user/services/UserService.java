package io.github.denkoch.mycosts.user.services;

import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.exceptions.ResourceAlreadyExistsException;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.payment.repositories.PaymentRepository;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private PaymentRepository paymentRepository;

    public UserService(UserRepository userRepository, CategoryRepository categoryRepository,
                       PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.categoryRepository = categoryRepository;
    }

    public Collection<User> readUsers() {
        return userRepository.findAll();
    }

    public Optional<User> readUser(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User {" + id + "} not found!"));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        return user;
    }

    @Transactional
    public void deleteUser(UUID id) {
        paymentRepository.deleteAllByUserId(id);
        categoryRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
