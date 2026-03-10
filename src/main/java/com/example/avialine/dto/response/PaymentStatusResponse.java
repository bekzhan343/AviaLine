package com.example.avialine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatusResponse implements Serializable {




    @JsonProperty("payment_status")
    private String paymentStatus;



}
