package com.example.avialine.dto.response;

import com.example.avialine.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayRefundResponse implements Serializable {

    @JsonProperty("payment_id")
    private Integer paymentId;

    private String message;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

}
