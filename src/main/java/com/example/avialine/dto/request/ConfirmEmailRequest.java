package com.example.avialine.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmEmailRequest implements Serializable {

    @Email(message = "invalid email or incorrect email format!")
    private String email;


}
