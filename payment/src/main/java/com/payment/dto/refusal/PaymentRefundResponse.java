package com.payment.dto.refusal;

import com.payment.model.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRefundResponse {
    private Long paymentId;
    private PaymentStatus status;
    private String message;
}
