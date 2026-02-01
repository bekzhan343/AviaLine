package com.example.avialine.dto.response;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class DefaultResponse {

    private boolean response;

    private String message;
}
