package com.example.avialine.service.impl;

import com.example.avialine.model.entity.Payment;
import com.example.avialine.service.MockGatewayService;
import org.springframework.stereotype.Service;

@Service
public class MockGatewayServiceImpl implements MockGatewayService {
    @Override
    public boolean process(Payment payment) {
        return Math.random() > 0.2;
    }
}
