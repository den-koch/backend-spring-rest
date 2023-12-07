package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Payment {
    private UUID id;
    private LocalDateTime dateTime;
    private Double moneyAmount;
    private UUID userId;
    private UUID categoryId;
    private PaymentType paymentType;
}
