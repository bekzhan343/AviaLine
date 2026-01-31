package com.example.avialine.dto.response;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmEmailResponse implements Serializable {

    private String email;

    private String message;
}
