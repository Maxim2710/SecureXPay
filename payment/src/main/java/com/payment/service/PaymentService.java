package com.payment.service;

import com.payment.connector.AuthConnector;
import com.payment.dto.confirmation.PaymentConfirmationResponse;
import com.payment.dto.PaymentDTO;
import com.payment.dto.history.PaymentHistoryDTO;
import com.payment.model.payment.Payment;
import com.payment.model.status.PaymentStatus;
import com.payment.model.user.User;
import com.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthConnector authConnector;

    public PaymentDTO createPayment(String token, BigDecimal amount) {
        User user = authConnector.getCurrentUser(token);

        String otp = generateOtp();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOtp(otp);

        payment = paymentRepository.save(payment);

        emailService.sendOtpEmail(payment.getId(), user.getEmail(), otp, amount);

        return new PaymentDTO(
                payment.getId(),
                payment.getStatus()
        );
    }

    public PaymentConfirmationResponse confirmPayment(Long paymentId, String otp) {
        Optional<Payment> optionalPayment = paymentRepository.findByIdWithUser(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Платеж не найден");
        }

        Payment payment = optionalPayment.get();
        String userEmail = payment.getUser().getEmail();

        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            throw new IllegalArgumentException("Платеж уже подтвержден");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Платеж можно подтвердить только в статусе PENDING");
        }

        if (!payment.getOtp().equals(otp)) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            emailService.sendPaymentFailedEmail(payment.getId(), userEmail, payment.getAmount());

            throw new IllegalArgumentException("Неверный одноразовый код");
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

        emailService.sendPaymentConfirmationEmail(payment.getId(), userEmail, payment.getAmount());

        return new PaymentConfirmationResponse(payment.getId(),
                payment.getStatus(),
                "Платеж успешно подтвержден"
        );
    }

    @Transactional
    public void cancelPayment(String token, Long paymentId) {
        User user = authConnector.getCurrentUser(token);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Платеж не найден");
        }

        Payment payment = optionalPayment.get();

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Вы не можете отменить платеж другого пользователя");
        }

        if (payment.getStatus() == PaymentStatus.CANCELED) {
            throw new IllegalArgumentException("Платеж уже отменен");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Платеж можно отменить только в статусе PENDING");
        }

        payment.setStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);

        emailService.sendPaymentCancelledEmail(payment.getId(), payment.getUser().getEmail(), payment.getAmount());
    }

    @Transactional
    public void refundPayment(String token, Long paymentId) {
        User user = authConnector.getCurrentUser(token);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Платеж не найден");
        }

        Payment payment = optionalPayment.get();

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Вы не можете вернуть платеж другого пользователя");
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException("Платеж уже возвращен");
        }

        if (payment.getStatus() != PaymentStatus.CONFIRMED) {
            throw new IllegalArgumentException("Платеж можно вернуть только в статусе CONFIRMED");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        emailService.sendPaymentRefundedEmail(payment.getId(), payment.getUser().getEmail(), payment.getAmount());
    }

    @Transactional
    public List<PaymentHistoryDTO> getPaymentHistory(String token) {
        User user = authConnector.getCurrentUser(token);

        List<Payment> payments = paymentRepository.findByUserId(user.getId());

        return payments.stream()
                .map(payment -> new PaymentHistoryDTO(
                        payment.getId(),
                        payment.getAmount(),
                        payment.getStatus(),
                        payment.getCreatedAt(),
                        payment.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentStatus(String token, Long paymentId) {
        User user = authConnector.getCurrentUser(token);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Платеж не найден");
        }

        Payment payment = optionalPayment.get();

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Вы не можете просматривать статус чужого платежа");
        }

        return new PaymentDTO(
                payment.getId(),
                payment.getStatus()
        );
    }

    private String generateOtp() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            otp.append(characters.charAt(random.nextInt(characters.length())));
        }

        return otp.toString();
    }
}
