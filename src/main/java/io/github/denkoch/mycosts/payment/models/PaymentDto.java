package io.github.denkoch.mycosts.payment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.models.CategoryDto;
import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
import io.github.denkoch.mycosts.user.models.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentDto {
    private UUID id;
    private LocalDateTime dateTime;
    private Double moneyAmount;
    private UUID categoryId;
    private PaymentType paymentType;
}
