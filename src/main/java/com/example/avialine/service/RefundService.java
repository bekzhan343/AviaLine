package com.example.avialine.service;

import com.example.avialine.dto.request.RefundQuoteRequest;
import com.example.avialine.dto.response.RefundQuoteResponse;

public interface RefundService {

    RefundQuoteResponse refundQuote(RefundQuoteRequest request);
}
