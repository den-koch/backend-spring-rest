package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.payment.models.Payment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface PaymentRepository {

    Payment findById(Long userId, Long id);

    Collection<Payment> findAll(Long userId);

    Payment save(Payment payment);

    void deleteById(Long userId, Long id);

    void deleteByUserId(Long id);
}
