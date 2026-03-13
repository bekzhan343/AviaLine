package com.example.avialine.dto.response;

import com.example.avialine.enums.OrderStatus;
import com.example.avialine.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllPaymentsByOrders implements Serializable {

    @JsonProperty("user_id")
    private Integer userId;

    private List<OrderPaymentSummary> orders;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderPaymentSummary implements Serializable {

        @JsonProperty("order_id")
        private Integer orderId;

        private String regnum;

        @JsonProperty("order_status")
        private OrderStatus orderStatus;

        @JsonProperty("total_amount")
        private BigDecimal totalAmount;

        private BigDecimal taxes;

        @JsonProperty("passengers_count")
        private Integer passengersCount;

        private List<PaymentDetailSummary> payments;

    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentDetailSummary implements Serializable {

        @JsonProperty("payment_id")
        private Integer paymentId;

        @JsonProperty("payment_status")
        private PaymentStatus paymentStatus;

        private BigDecimal amount;

        @JsonProperty("paid_amount")
        private BigDecimal paidAmount;


    }


}
