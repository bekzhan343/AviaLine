package com.example.avialine.service;

import com.example.avialine.dto.response.PaymentInitResponse;

public interface PaymentService {

    PaymentInitResponse initPayment(Integer orderId);

}
