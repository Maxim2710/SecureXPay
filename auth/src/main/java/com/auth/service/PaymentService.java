package com.auth.service;

import com.auth.model.user.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(String token) {
        try {
            String formatToken = token.substring(7);
            String email = jwtUtils.parseJwt(formatToken);

            Optional<User> currentUser = userRepository.findByEmail(email);

            if (currentUser.isEmpty()) {
                throw new RuntimeException("Пользователь не найден");
            }

            return currentUser.get();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении текущего пользователя", e);
        }
    }
}
