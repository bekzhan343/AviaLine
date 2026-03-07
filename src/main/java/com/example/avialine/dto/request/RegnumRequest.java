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
public class RegnumRequest implements Serializable {

    @Length(min = 1, max = 100)
    @NotBlank(message = "regnum cannot be empty!")
    private String regnum;


}
