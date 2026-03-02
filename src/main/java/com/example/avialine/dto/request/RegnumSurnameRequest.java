package com.example.avialine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegnumSurnameRequest implements Serializable {

    @NotBlank(message = "surname cannot be empty!")
    @Size(min = 1, message = "Surname cannot contain exactly 1 char!")
    private String surname;

    @NotBlank
    @Size(min = 1, max = 100, message = "Regnum must be at least 1 chars and maximum 100 chars!")
    private String regnum;

    private boolean moreInfo = true;

    private boolean addCommonStatus = true;
}
