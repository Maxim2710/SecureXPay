package com.payment.controller;

import com.payment.bom.error.ErrorResponse;
import com.payment.dto.*;
import com.payment.model.payment.Payment;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path = "/create")
    public ResponseEntity<Object> createPayment(@RequestHeader(name = "Authorization") String token,
                                           @RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentDTO payment = paymentService.createPayment(token, paymentRequest.getAmount());
            return ResponseEntity.ok(new PaymentResponse(payment.getId(), payment.getStatus()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmationRequest request) {
        try {
            System.out.println(1);
            PaymentConfirmationResponse response = paymentService.confirmPayment(request.getId(), request.getOtp());
            System.out.println(1);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Произошла непредвиденная ошибка"));
        }
    }
}
