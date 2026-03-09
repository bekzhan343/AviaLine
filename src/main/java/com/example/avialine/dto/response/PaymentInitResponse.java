package com.example.avialine.dto.response;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.PaymentStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitResponse implements Serializable {

    private Integer paymentId;

    private Integer orderId;

    private BigDecimal amount;

    private Currency currency;

    private PaymentStatus status;
}
