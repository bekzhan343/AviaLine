package com.example.avialine.dto.response;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmCodeResponse implements Serializable {

    private boolean response = true;

    private String message;

    private String token;
}
