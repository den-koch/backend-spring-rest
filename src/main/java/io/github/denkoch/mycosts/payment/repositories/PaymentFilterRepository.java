package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.payment.models.Payment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface PaymentFilterRepository extends PaymentRepository{

    Collection<Payment> findAllByDate(UUID userId, LocalDate before, LocalDate after);

    Collection<Payment> findAllByCategory(UUID userId, UUID categoryId);

    LocalDate findLowestDate(UUID userId);

    LocalDate findHighestDate(UUID userId);
}
