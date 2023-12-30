package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.user.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, UUID>, CrudRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p " +
            "WHERE p.user=?1 AND p.category=?2 AND p.dateTime " +
            "BETWEEN TO_DATE(?3, 'YYYY-MM-DD') AND TO_DATE(?4, 'YYYY-MM-DD')")
    List<Payment> findAllByCategoryAndDate(User user, Category categoryId,
                                           String after, String before, Pageable pageable);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.user=?1 AND p.dateTime " +
            "BETWEEN TO_DATE(?2, 'YYYY-MM-DD') AND TO_DATE(?3, 'YYYY-MM-DD')")
    List<Payment> findAllByDate(User user, String after, String before, Pageable pageable);


    LocalDate findLowestDate(User user);

    Optional<Payment> findByUserAndId(User user, UUID id);

    void deleteByUserAndId(User user, UUID id);

    void deleteAllByUserId(UUID userId);
}
