package io.github.denkoch.mycosts.user.services;

import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.payment.repositories.PaymentRepository;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class UserService {

    private CategoryRepository categoryRepository;
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    public UserService(CategoryRepository categoryRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public Collection<User> readUsers() {
        return userRepository.findAll();
    }

    public User readUser(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        paymentRepository.deleteByUserId(id);
        categoryRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
