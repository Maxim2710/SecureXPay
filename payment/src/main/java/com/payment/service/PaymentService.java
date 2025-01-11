package com.payment.service;

import com.payment.connector.AuthConnector;
import com.payment.dto.PaymentConfirmationResponse;
import com.payment.dto.PaymentDTO;
import com.payment.dto.PaymentResponse;
import com.payment.model.payment.Payment;
import com.payment.model.status.PaymentStatus;
import com.payment.model.user.User;
import com.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Optional;

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

        return new PaymentDTO(payment.getId(), payment.getStatus());
    }

    public PaymentConfirmationResponse confirmPayment(Long paymentId, String otp) {
        Optional<Payment> optionalPayment = paymentRepository.findByIdWithUser(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Платеж не найден");
        }

        Payment payment = optionalPayment.get();
        String userEmail = payment.getUser().getEmail();

        if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new IllegalArgumentException("Платеж уже отклонен");
        }

        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            throw new IllegalArgumentException("Платеж уже подтвержден");
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

        return new PaymentConfirmationResponse(payment.getId(), payment.getStatus(), "Платеж успешно подтвержден");
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
