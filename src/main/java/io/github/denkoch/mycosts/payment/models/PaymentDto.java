package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class PaymentDto {
    private UUID id;
    private LocalDateTime dateTime;
    private Double moneyAmount;
    private UUID categoryId;
    private PaymentType paymentType;
}
