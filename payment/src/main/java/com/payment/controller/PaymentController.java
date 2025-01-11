package com.payment.controller;

import com.payment.bom.error.ErrorResponse;
import com.payment.dto.*;
import com.payment.model.status.PaymentStatus;
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
            PaymentConfirmationResponse response = paymentService.confirmPayment(request.getId(), request.getOtp());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Произошла непредвиденная ошибка"));
        }
    }

    @PostMapping(path = "/cancel")
    public ResponseEntity<Object> cancelPayment(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam Long paymentId) {
        try {
            paymentService.cancelPayment(token, paymentId);
            return ResponseEntity.ok(new PaymentCanceledResponse(paymentId, PaymentStatus.CANCELED, "Платеж успешно отменен"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Произошла непредвиденная ошибка"));
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<Object> refundPayment(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam Long paymentId) {
        try {
            paymentService.refundPayment(token, paymentId);
            return ResponseEntity.ok(new PaymentRefundResponse(paymentId, PaymentStatus.REFUNDED, "Платеж успешно возвращен"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Произошла непредвиденная ошибка"));
        }
    }
}
