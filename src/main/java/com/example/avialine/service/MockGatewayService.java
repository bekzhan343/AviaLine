package com.example.avialine.service;

import com.example.avialine.model.entity.Payment;

public interface MockGatewayService {

    boolean process(Payment payment);
}
