package com.payment.repository;

import com.payment.model.payment.Payment;
import com.payment.model.status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN FETCH p.user WHERE p.id = :paymentId")
    Optional<Payment> findByIdWithUser(@Param("paymentId") Long paymentId);

    List<Payment> findByUserId(Long userId);

    List<Payment> findAllByStatusInAndUpdatedAtBefore(List<PaymentStatus> statuses, LocalDateTime updatedAt);

    List<Payment> findAllByStatusAndUpdatedAtBefore(PaymentStatus status, LocalDateTime updatedAt);
}
