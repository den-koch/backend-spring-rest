package io.github.denkoch.mycosts.payment.services;

import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.repositories.InMemoryPaymentRepository;
import io.github.denkoch.mycosts.payment.repositories.PaymentFilterRepository;
import io.github.denkoch.mycosts.payment.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Service
public class PaymentService {

    private PaymentFilterRepository paymentRepository;

    public PaymentService(PaymentFilterRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Collection<Payment> readPayments(UUID userId, LocalDate before, LocalDate after, UUID categoryId, Long page) {

        // values per page
        Long perPage = 3L;

        if (before == null) before = paymentRepository.findLowestDate(userId);
        if (after == null) after = paymentRepository.findHighestDate(userId);

        Collection<Payment> byCategoryCollection;
        if (categoryId != null) {
            byCategoryCollection = paymentRepository.findAllByCategory(userId, categoryId);
        } else {
            byCategoryCollection = paymentRepository.findAll(userId);
        }

        Collection<Payment> byDateCollection = paymentRepository.findAllByDate(userId, before, after);

        return byCategoryCollection.stream().filter(byDateCollection::contains).skip(page*perPage).limit(perPage).toList();
    }

    public Payment readPayment(UUID userId, UUID id) {
        return paymentRepository.findById(userId, id);
    }

    public Payment createPayment(Payment payment) {
        UUID id = UUID.randomUUID();
        payment.setId(id);
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(UUID userId, UUID id) {
        paymentRepository.deleteById(userId, id);
    }


}
