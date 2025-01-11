package com.auth.service;

import com.auth.bom.UserBom;
import com.auth.converter.UserConverter;
import com.auth.dto.login.AccountLoginForm;
import com.auth.dto.registration.AccountRegistrationForm;
import com.auth.dto.login.AccountResponseLogin;
import com.auth.dto.registration.AccountResponseRegister;
import com.auth.model.role.Role;
import com.auth.model.user.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public AccountResponseRegister registerUser(AccountRegistrationForm registrationForm) {
        if (userRepository.existsByEmail(registrationForm.getEmail())) {
            throw new RuntimeException("Email уже занят");
        }

        User newUser = new User();

        newUser.setUsername(registrationForm.getUsername());
        newUser.setEmail(registrationForm.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(registrationForm.getPassword()));
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);

        String token = jwtUtils.generateToken(savedUser.getEmail());

        UserBom userBom = UserConverter.convertToBom(savedUser);

        return new AccountResponseRegister("Регистрация успешна", token, userBom);

    }

    public AccountResponseLogin loginUser(AccountLoginForm accountLoginForm) {
        User user = userRepository.findByEmail(accountLoginForm.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден"));

        if (!passwordEncoder.matches(accountLoginForm.getPassword(), user.getHashedPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        UserBom userBom = UserConverter.convertToBom(user);

        return new AccountResponseLogin("Авторизация успешна", token, userBom);
    }

}
