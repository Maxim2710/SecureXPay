package com.payment.service;

import com.payment.model.payment.Payment;
import com.payment.model.status.PaymentStatus;
import com.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentCleanupService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanupOldPayments() {
        LocalDateTime thresholdTime = LocalDateTime.now().minusHours(16);

        List<Payment> oldPayments = paymentRepository.findAllByStatusInAndUpdatedAtBefore(
                List.of(PaymentStatus.CANCELED, PaymentStatus.FAILED, PaymentStatus.REFUNDED), thresholdTime
        );

        if (!oldPayments.isEmpty()) {
            paymentRepository.deleteAll(oldPayments);
        }
    }
}
