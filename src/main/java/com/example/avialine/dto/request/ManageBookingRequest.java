package com.example.avialine.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageBookingRequest implements Serializable {

    @Length(min = 1, max = 6)
    @NotBlank(message = "regnum cannot be empty!")
    private String regnum;

    @Length(min = 1, max = 100)
    @NotBlank(message = "surname cannot be empty!")
    private String surname;

}
