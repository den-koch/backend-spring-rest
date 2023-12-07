package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.payment.models.Payment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface PaymentRepository {

    Payment findById(UUID userId, UUID id);

    Collection<Payment> findAll(UUID userId);

    Payment save(Payment payment);

    void deleteById(UUID userId, UUID id);
}
