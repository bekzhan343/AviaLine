package com.example.avialine.controller;

import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("${end.point.payment-init}")
    public ResponseEntity<PaymentInitResponse> initPayment(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.status(200).body(paymentService.initPayment(orderId));
    }
}
