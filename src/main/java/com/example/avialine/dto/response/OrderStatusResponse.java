package com.example.avialine.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderStatusResponse implements Serializable {

    private Integer id;

    private String status;

}
