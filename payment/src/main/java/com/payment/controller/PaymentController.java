package com.payment.controller;

import com.payment.bom.error.ErrorResponse;
import com.payment.dto.*;
import com.payment.dto.cancellation.PaymentCanceledResponse;
import com.payment.dto.confirmation.PaymentConfirmationRequest;
import com.payment.dto.confirmation.PaymentConfirmationResponse;
import com.payment.dto.creature.PaymentRequest;
import com.payment.dto.creature.PaymentResponse;
import com.payment.dto.history.PaymentHistoryDTO;
import com.payment.dto.refusal.PaymentRefundResponse;
import com.payment.model.status.PaymentStatus;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
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

    @GetMapping("/history")
    public ResponseEntity<List<PaymentHistoryDTO>> getPaymentHistory(@RequestHeader(name = "Authorization") String token) {
        List<PaymentHistoryDTO> history = paymentService.getPaymentHistory(token);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<Object> getPaymentStatus(@RequestHeader(name = "Authorization") String token,
                                                   @PathVariable Long paymentId) {
        try {
            PaymentDTO statusDTO = paymentService.getPaymentStatus(token, paymentId);
            return ResponseEntity.ok(statusDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Произошла ошибка при получении статуса платежа"));
        }
    }
}
