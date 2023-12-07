package io.github.denkoch.mycosts.config.mappers;

import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.models.PaymentCreationDto;
import io.github.denkoch.mycosts.payment.models.PaymentDto;

public class PaymentMapper {
    public Payment dtoToPayment(PaymentCreationDto paymentCreationDto) {
        return new Payment(null, paymentCreationDto.getDateTime(), paymentCreationDto.getMoneyAmount(),
                paymentCreationDto.getUserId(), paymentCreationDto.getCategoryId(), paymentCreationDto.getPaymentType());
    }

    public PaymentDto paymentToDto(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getDateTime(), payment.getMoneyAmount(),
                payment.getCategoryId(), payment.getPaymentType());
    }
}
