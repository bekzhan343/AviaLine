package com.example.avialine.dto.request;

import com.example.avialine.enums.OperationMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefundQuoteRequest implements Serializable {

    @Length(min = 1, max = 6)
    @NotBlank
    private String regnum;

    @Length(min = 1, max = 60)
    @NotBlank
    private String surname;

    @JsonProperty("full_refund")
    private Boolean fullRefund = false;

    @JsonProperty("passenger_ids")
    private List<Integer> passengerIds;

    @JsonProperty("operation_mode")
    private OperationMode operationMode = OperationMode.VOLUNTARY;

    private Boolean pretend = true;
}