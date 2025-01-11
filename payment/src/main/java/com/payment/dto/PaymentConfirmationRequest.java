package com.payment.dto;

import lombok.Data;

@Data
public class PaymentConfirmationRequest {
    private Long id;
    private String otp;
}
