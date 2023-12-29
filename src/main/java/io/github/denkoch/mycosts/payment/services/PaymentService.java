package io.github.denkoch.mycosts.payment.services;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.models.PaymentRequestDto;
import io.github.denkoch.mycosts.payment.repositories.PaymentRepository;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    public CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Collection<Payment> readPayments(User user, LocalDate before, LocalDate after, UUID categoryId, Integer page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pageable pageable = PageRequest.of(page, 3);

        if (before == null) before = LocalDate.now();
        if (after == null) after = paymentRepository.findLowestDate(user);

        Collection<Payment> byCategoryCollection;

        Optional<Category> category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId);
        }

        if (category != null && category.isPresent()) {
            byCategoryCollection = paymentRepository.findAllByCategoryAndDate(user, category.get(),
                    after.format(formatter), before.format(formatter), pageable);
        } else {
            byCategoryCollection = paymentRepository.findAllByDate(user,
                    after.format(formatter), before.format(formatter), pageable);
        }

        return byCategoryCollection;
    }

    public Optional<Payment> readPayment(User user, UUID id) {
        return paymentRepository.findByUserAndId(user, id);
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updatePayment(UUID userId, UUID id, PaymentRequestDto paymentRequestDto) {

        UUID categoryId = paymentRequestDto.getCategoryId();

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Category category = categoryRepository.findByUserAndId(user, categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category {" + categoryId + "} not found"));

        Payment payment = paymentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Payment {" + id + "} not found!"));

        payment.setDateTime(paymentRequestDto.getDateTime());
        payment.setMoneyAmount(paymentRequestDto.getMoneyAmount());
        payment.setPaymentType(paymentRequestDto.getPaymentType());
        payment.setUser(user);
        payment.setCategory(category);

        return paymentRepository.save(payment);
    }

    @Transactional
    public void deletePayment(UUID userId, UUID id) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User {" + "} not found"));

        paymentRepository.deleteByUserAndId(user, id);
    }


}
