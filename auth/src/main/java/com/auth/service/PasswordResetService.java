package com.auth.service;

import com.auth.model.user.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден"));

        String token = jwtUtils.generateToken(email);
        String resetLink = "http://localhost:8080/auth/reset-password-confirm?token=" + token;

        String username = user.getUsername();

        String subject = "Сброс пароля для вашей учетной записи SecureXPay";

        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #4CAF50;'>Здравствуйте, " + username + "!</h2>" +
                "<p style='color: #333; font-size: 16px;'>Мы получили запрос на сброс пароля для вашей учетной записи SecureXPay.</p>" +
                "<p style='color: #333; font-size: 16px;'>Для того чтобы сбросить пароль, перейдите по ссылке ниже:</p>" +
                "<p style='font-size: 18px; font-weight: bold; color: #1a73e8;'><a href=\"" + resetLink + "\" target=\"_blank\">Сбросить пароль</a></p>" +
                "<p style='color: #333; font-size: 16px;'>Если вы не запрашивали сброс пароля, просто проигнорируйте это письмо. Мы рекомендуем не делиться этим письмом с кем-либо еще для вашей безопасности.</p>" +
                "<p style='color: #333; font-size: 16px;'>С уважением,</p>" +
                "<p style='color: #333; font-size: 16px;'>Команда SecureXPay</p>" +
                "<hr style='border: 1px solid #e0e0e0;'/>" +
                "<footer style='color: #999; font-size: 14px;'>Это автоматическое письмо. Пожалуйста, не отвечайте на него.</footer>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(email, subject, content);
    }


    public void resetPassword(String token, String newPassword) {
        String email;
        try {
            email = jwtUtils.parseJwt(token);
        } catch (Exception e) {
            throw new RuntimeException("Неверный или истекший токен");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
