package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.payment.models.Payment;

import java.time.LocalDate;
import java.util.Collection;

public interface PaymentFilterRepository extends PaymentRepository{

    Collection<Payment> findAllByDate(Long userId, LocalDate before, LocalDate after);

    Collection<Payment> findAllByCategory(Long userId, Long categoryId);

    LocalDate findLowestDate(Long userId);
}
