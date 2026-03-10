package com.example.avialine.service;

import com.example.avialine.dto.response.PayResponse;
import com.example.avialine.dto.response.PaymentInitResponse;
import com.example.avialine.dto.response.PaymentStatusResponse;

public interface PaymentService {

    PaymentInitResponse initPayment(Integer orderId);

    PaymentStatusResponse paymentStatus(Integer paymentId);

    PayResponse pay(Integer paymentId);

    PayResponse retry(Integer paymentId);
}
