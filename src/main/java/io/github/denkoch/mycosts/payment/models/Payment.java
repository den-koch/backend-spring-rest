package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import io.github.denkoch.mycosts.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "payments")
@NamedQuery(name = "Payment.findLowestDate", query = "SELECT MIN(p.dateTime) FROM Payment p WHERE p.user = ?1")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime dateTime;

    private Double moneyAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
