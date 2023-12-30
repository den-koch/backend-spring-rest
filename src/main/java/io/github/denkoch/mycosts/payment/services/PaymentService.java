package io.github.denkoch.mycosts.payment.services;

import io.github.denkoch.mycosts.category.repositories.CategoryRepository;
import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.repositories.PaymentFilterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private PaymentFilterRepository paymentRepository;
    private CategoryRepository categoryRepository;

    public PaymentService(PaymentFilterRepository paymentRepository, CategoryRepository categoryRepository) {
        this.paymentRepository = paymentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Collection<Payment> readPayments(Long userId, LocalDate before, LocalDate after, Long categoryId, Long page) {

        // values per page
        Long perPage = 3L;

        if (before == null) before = LocalDate.now();
        if (after == null) after = paymentRepository.findLowestDate(userId);

        Collection<Payment> byCategoryCollection;
        if (categoryId != null) {
            byCategoryCollection = paymentRepository.findAllByCategory(userId, categoryId);
        } else {
            byCategoryCollection = paymentRepository.findAll(userId);
        }

        Collection<Payment> byDateCollection = paymentRepository.findAllByDate(userId, before, after);

        return byDateCollection.stream().filter(byCategoryCollection::contains).skip(page * perPage).
                limit(perPage).collect(Collectors.toList());
    }

    public Payment readPayment(Long userId, Long id) {
        return paymentRepository.findById(userId, id);
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        categoryRepository.findById(payment.getUserId(), payment.getCategoryId());
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updatePayment(Payment payment) {
        categoryRepository.findById(payment.getUserId(), payment.getCategoryId());
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long userId, Long id) {
        paymentRepository.deleteById(userId, id);
    }


}
