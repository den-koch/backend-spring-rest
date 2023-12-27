package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class PaymentCreationDto {
    @PastOrPresent
    private LocalDateTime dateTime;
    @Positive
    private Double moneyAmount;
    @NotNull
    private Long userId;
    @NotNull
    private Long categoryId;
    @NotNull
    private PaymentType paymentType;
}
