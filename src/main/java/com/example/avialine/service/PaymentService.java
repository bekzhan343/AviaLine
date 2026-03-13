package com.example.avialine.service;

import com.example.avialine.dto.request.RefundQuoteRequest;
import com.example.avialine.dto.response.*;

public interface PaymentService {

    PaymentInitResponse initPayment(Integer orderId);

    PaymentStatusResponse paymentStatus(Integer paymentId);

    PayResponse pay(Integer paymentId);

    PayResponse retry(Integer paymentId);

    PayCancelResponse cancel(Integer paymentId);

    RefundQuoteResponse refundQuote(RefundQuoteRequest request);

    GetPaymentsByOrderIdResponse getPaymentsByOrderId(Integer orderId);

    GetAllPaymentsByOrders paymentsHistoryByOrder();
}
