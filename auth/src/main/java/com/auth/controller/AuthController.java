package com.auth.controller;

import com.auth.bom.error.ErrorResponse;
import com.auth.dto.AccountLoginForm;
import com.auth.dto.AccountRegistrationForm;
import com.auth.dto.AccountResponseLogin;
import com.auth.dto.AccountResponseRegister;
import com.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

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
}
