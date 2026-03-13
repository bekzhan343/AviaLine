package com.example.avialine.dto.response;

import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPaymentsByOrderIdResponse implements Serializable {

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    private Set<PaymentResponse> payments;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentResponse implements Serializable {

        @JsonProperty("payment_id")
        private Integer paymentId;

        @JsonProperty("payment_status")
        private PaymentStatus paymentStatus;

        private BigDecimal amount;

        @JsonProperty("paid_amount")
        private BigDecimal paidAmount;


    }



}
