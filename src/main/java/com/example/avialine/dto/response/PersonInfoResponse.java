package com.example.avialine.dto.response;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class PersonInfoResponse implements Serializable {

    private String email;
    private String username;
    private String phone;

}
