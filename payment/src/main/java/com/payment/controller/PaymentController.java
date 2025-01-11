package com.payment.controller;

import com.payment.bom.error.ErrorResponse;
import com.payment.dto.PaymentDTO;
import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.model.payment.Payment;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPayment(@RequestHeader(name = "Authorization") String token,
                                           @RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentDTO payment = paymentService.createPayment(token, paymentRequest.getAmount());
            return ResponseEntity.ok(new PaymentResponse(payment.getId(), payment.getStatus()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        }
    }
}
