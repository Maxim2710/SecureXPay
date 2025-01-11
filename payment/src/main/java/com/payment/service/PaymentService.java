package com.payment.service;

import com.payment.connector.AuthConnector;
import com.payment.model.payment.Payment;
import com.payment.model.status.PaymentStatus;
import com.payment.model.user.User;
import com.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthConnector authConnector;

    public Payment createPayment(String token, BigDecimal amount) {
        User user = authConnector.getCurrentUser(token);

        String otp = generateOtp();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOtp(otp);

        payment = paymentRepository.save(payment);

        emailService.sendOtpEmail(user.getEmail(), otp, amount);

        return payment;
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
