package com.auth.controller;

import com.auth.model.user.User;
import com.auth.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getCurrentUser")
    public User getCurrentUser(@RequestHeader(name = "Authorization") String token) {
        return paymentService.getCurrentUser(token);
    }
}
