package io.github.denkoch.mycosts.payment.models;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import io.github.denkoch.mycosts.user.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentRequestDto {
    @PastOrPresent
    private LocalDateTime dateTime;

    @Positive
    private Double moneyAmount;

    @NotNull
    private PaymentType paymentType;

    @NotNull
    private UUID categoryId;
}
