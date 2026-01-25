package com.example.avialine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleDTO implements Serializable {


    private Integer id;

    @NotBlank(message = "name of role cannot be empty!")
    private String name;
}
