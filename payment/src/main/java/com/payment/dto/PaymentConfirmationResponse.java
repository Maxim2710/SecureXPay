package com.payment.dto;

import com.payment.model.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentConfirmationResponse {
    private Long paymentId;
    private PaymentStatus status;
    private String message;
}
