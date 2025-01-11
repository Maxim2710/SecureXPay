package com.auth.controller;

import com.auth.bom.error.ErrorResponse;
import com.auth.dto.login.AccountLoginForm;
import com.auth.dto.login.AccountResponseLogin;
import com.auth.dto.passwordreset.NewPasswordRequest;
import com.auth.dto.passwordreset.PasswordResetRequest;
import com.auth.dto.passwordupdate.UpdatePasswordRequest;
import com.auth.dto.registration.AccountRegistrationForm;
import com.auth.dto.registration.AccountResponseRegister;
import com.auth.service.AuthService;
import com.auth.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> registerUser(@RequestBody AccountRegistrationForm accountRegistrationForm) {
        try {
            AccountResponseRegister response = authService.registerUser(accountRegistrationForm);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@RequestBody AccountLoginForm accountLoginForm) {
        try {
            AccountResponseLogin responseLogin = authService.loginUser(accountLoginForm);
            return ResponseEntity.ok(responseLogin);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> sendResetPasswordLink(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            passwordResetService.sendResetPasswordLink(passwordResetRequest.getEmail());  // используем email из DTO
            return ResponseEntity.ok("Ссылка для сброса пароля отправлена на email");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password-confirm")
    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
                                                @RequestBody NewPasswordRequest newPasswordRequest) {
        try {
            passwordResetService.resetPassword(token, newPasswordRequest.getNewPassword());
            return ResponseEntity.ok("Пароль успешно изменен");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PutMapping(path = "/update-password")
    public ResponseEntity<Object> updatePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        System.out.println(token);
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            passwordResetService.updatePassword(jwtToken, updatePasswordRequest.getCurrentPassword(), updatePasswordRequest.getNewPassword());
            return ResponseEntity.ok("Пароль успешно обновлен");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }
}
