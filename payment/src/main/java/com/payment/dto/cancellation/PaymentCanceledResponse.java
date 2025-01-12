package com.payment.dto.cancellation;

import com.payment.model.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCanceledResponse {
    private Long id;
    private PaymentStatus status;
    private String message;
}
