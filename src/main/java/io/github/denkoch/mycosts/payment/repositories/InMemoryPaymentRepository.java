package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.payment.models.Payment;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryPaymentRepository implements PaymentFilterRepository {

    private Map<UUID, Payment> repository = new TreeMap<>();

    @Override
    public Payment findById(UUID userId, UUID id) {
        return repository.values().stream()
                .filter(payment -> userId.equals(payment.getUserId()) && id.equals(payment.getId())).findAny().orElse(null);
    }

    @Override
    public Collection<Payment> findAll(UUID userId) {
        return repository.values().stream().filter(payment -> userId.equals(payment.getUserId())).toList();
    }

    @Override
    public Payment save(Payment payment) {
        repository.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public void deleteById(UUID userId, UUID id) {
        if (findById(userId, id) != null)
            repository.remove(id);
    }

    @Override
    public Collection<Payment> findAllByDate(UUID userId, LocalDate before, LocalDate after){
        return repository.values().stream().filter(payment -> userId.equals(payment.getUserId()))
                .filter(payment -> !payment.getDateTime().toLocalDate().isBefore(before)
                        && !payment.getDateTime().toLocalDate().isAfter(after)).toList();
    }

    @Override
    public Collection<Payment> findAllByCategory(UUID userId, UUID categoryId) {
        return repository.values().stream().filter(payment -> userId.equals(payment.getUserId())
                && categoryId.equals(payment.getCategoryId())).toList();
    }

    @Override
    public LocalDate findLowestDate(UUID userId){
        Optional<LocalDateTime> lowestDate = repository.values().stream()
                .filter(payment -> userId.equals(payment.getUserId()))
                .map(Payment::getDateTime).min(Comparator.comparing(LocalDateTime::toLocalDate));

        if (lowestDate.isEmpty())
            return LocalDate.now();
        return lowestDate.get().toLocalDate();
    }

    @Override
    public LocalDate findHighestDate(UUID userId){
        Optional<LocalDateTime> highestDate = repository.values().stream()
                .filter(payment -> userId.equals(payment.getUserId()))
                .map(Payment::getDateTime).max(Comparator.comparing(LocalDateTime::toLocalDate));

        if (highestDate.isEmpty())
            return LocalDate.now();
        return highestDate.get().toLocalDate();

    }

}
