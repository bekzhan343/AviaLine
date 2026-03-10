package com.example.avialine.dto.response;

import com.example.avialine.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayResponse {

    private Integer paymentId;

    private PaymentStatus status;

    private BigDecimal amount;

    private BigDecimal paidAmount;

}
