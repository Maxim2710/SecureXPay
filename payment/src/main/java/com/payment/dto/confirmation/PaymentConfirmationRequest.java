package com.payment.dto.confirmation;

import lombok.Data;

@Data
public class PaymentConfirmationRequest {
    private Long id;
    private String otp;
}
