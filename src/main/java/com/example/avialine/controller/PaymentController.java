package com.example.avialine.controller;

import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.exception.DataNotFoundException;
import com.example.avialine.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${end.point.payment-base}")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("${end.point.payment-init}")
    public ResponseEntity<?> initPayment(@PathVariable("orderId") Integer orderId) {
        try {
            return ResponseEntity.status(200).body(paymentService.initPayment(orderId));
        } catch (IllegalStateException | DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("${end.point.payment-status}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.paymentStatus(paymentId));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("${end.point.payment-pay}")
    public ResponseEntity<?> pay(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.pay(paymentId));
        } catch (DataNotFoundException | IllegalStateException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("${end.point.payment-retry}")
    public ResponseEntity<?> retry(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.retry(paymentId));
        } catch (IllegalStateException | DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("${end.point.payment-cancel}")
    public ResponseEntity<?> cancel(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.cancel(paymentId));
        } catch (IllegalStateException | DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("${end.point.payment-refund}")
    public ResponseEntity<?> refund(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.refund(paymentId));
        } catch (IllegalStateException | DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("${end.point.payment-order-history-by-orderid}")
    public ResponseEntity<?> getPayments(@PathVariable("orderId") Integer orderId) {
        try {
            return ResponseEntity.status(200).body(paymentService.getPaymentsByOrderId(orderId));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("${end.point.payment-orders-history}")
    public ResponseEntity<?> getPaymentsHistory() {
        return ResponseEntity.status(200).body(paymentService.paymentsHistoryByOrder());
    }

}