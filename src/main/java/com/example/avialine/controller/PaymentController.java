package com.example.avialine.controller;

import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.dto.response.PaymentStatusResponse;
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

    @GetMapping("${end.point.payment-init}")
    public ResponseEntity<PaymentInitResponse> initPayment(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.status(200).body(paymentService.initPayment(orderId));
    }

    @GetMapping("${end.point.payment-status}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.paymentStatus(paymentId));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("${end.point.payment-pay}")
    public ResponseEntity<?> pay(@PathVariable("paymentId") Integer paymentId) {
        try {
            return ResponseEntity.status(200).body(paymentService.pay(paymentId));
        }catch (DataNotFoundException | IllegalStateException e){
            return ResponseEntity.status(404).body(new DetailErrorResponse(e.getMessage()));
        }
    }

}
