package com.payment.dto.history;

import com.payment.model.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentHistoryDTO {
    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
