package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Payment {
    private Long id;
    private Double moneyAmount;
    private LocalDateTime dateTime;
    private Long userId;
    private Long categoryId;
    private PaymentType paymentType;
}
