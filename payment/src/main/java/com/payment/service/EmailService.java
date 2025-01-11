package com.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.from}")
    private String from;

    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка при отправке email", e);
        }
    }

    public void sendOtpEmail(Long paymentId, String email, String otp, BigDecimal amount) {
        String subject = "Ваш одноразовый код для подтверждения";

        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #4CAF50;'>Ваш одноразовый код подтверждения</h2>" +
                "<p style='color: #333; font-size: 16px;'>Мы получили запрос на выполнение действия, требующего подтверждения.</p>" +
                "<p style='color: #333; font-size: 16px;'>Ваш одноразовый код (OTP):</p>" +
                "<p style='font-size: 24px; font-weight: bold; color: #1a73e8;'>" + otp + "</p>" +
                "<p style='color: #333; font-size: 16px;'>Сумма операции: <strong>" + amount.setScale(2, RoundingMode.HALF_UP) + " руб.</strong></p>" +
                "<p style='color: #333; font-size: 16px;'>Уникальный идентификатор платежа: <strong>" + paymentId + "</strong></p>" +
                "<p style='color: #333; font-size: 16px;'>Введите этот код для завершения действия. Если вы не запрашивали данный код, просто проигнорируйте это письмо.</p>" +
                "<p style='color: #333; font-size: 16px;'>С уважением,</p>" +
                "<p style='color: #333; font-size: 16px;'>Команда SecureXPay</p>" +
                "<hr style='border: 1px solid #e0e0e0;'/>" +
                "<footer style='color: #999; font-size: 14px;'>Это автоматическое письмо. Пожалуйста, не отвечайте на него.</footer>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(email, subject, content);
    }

    public void sendPaymentConfirmationEmail(Long paymentId, String email, BigDecimal amount) {
        String subject = "Платеж успешно выполнен";

        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #4CAF50;'>Ваш платеж подтвержден</h2>" +
                "<p style='color: #333; font-size: 16px;'>Ваш платеж на сумму <strong>" + amount.setScale(2, RoundingMode.HALF_UP) + " руб.</strong> успешно завершен.</p>" +
                "<p style='color: #333; font-size: 16px;'>Уникальный идентификатор платежа: <strong>" + paymentId + "</strong></p>" +
                "<p style='color: #333; font-size: 16px;'>Спасибо за использование SecureXPay!</p>" +
                "<p style='color: #333; font-size: 16px;'>С уважением,</p>" +
                "<p style='color: #333; font-size: 16px;'>Команда SecureXPay</p>" +
                "<hr style='border: 1px solid #e0e0e0;'/>" +
                "<footer style='color: #999; font-size: 14px;'>Это автоматическое письмо. Пожалуйста, не отвечайте на него.</footer>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(email, subject, content);
    }

    public void sendPaymentFailedEmail(Long paymentId, String email, BigDecimal amount) {
        String subject = "Неудачная попытка подтверждения платежа";

        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #FF5733;'>Ошибка подтверждения платежа</h2>" +
                "<p style='color: #333; font-size: 16px;'>Вы ввели неверный одноразовый код (OTP) для подтверждения платежа.</p>" +
                "<p style='color: #333; font-size: 16px;'>Сумма операции: <strong>" + amount.setScale(2, RoundingMode.HALF_UP) + " руб.</strong></p>" +
                "<p style='color: #333; font-size: 16px;'>Уникальный идентификатор платежа: <strong>" + paymentId + "</strong></p>" +
                "<p style='color: #333; font-size: 16px;'>С уважением,</p>" +
                "<p style='color: #333; font-size: 16px;'>Команда SecureXPay</p>" +
                "<hr style='border: 1px solid #e0e0e0;'/>" +
                "<footer style='color: #999; font-size: 14px;'>Это автоматическое письмо. Пожалуйста, не отвечайте на него.</footer>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(email, subject, content);
    }

}


