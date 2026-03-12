package com.example.avialine.dto.response;

import com.example.avialine.enums.PaymentStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayCancelResponse implements Serializable {

    private Integer paymentId;

    private String message;

    private PaymentStatus paymentStatus;

}
