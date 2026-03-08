package com.example.avialine.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// RefundQuoteResponse
@Data
@Builder
public class RefundQuoteResponse {

    private String regnum;
    private String status;
    private String operationMode;
    private Boolean pretend;
    private BigDecimal totalRefundAmount;
    private List<PassengerRefund> passengers;

    @Data
    @Builder
    public static class PassengerRefund {
        private Integer passengerId;
        private String firstName;
        private String lastName;
        private BigDecimal refundAmount;
        private BigDecimal penaltyAmount;
    }
}