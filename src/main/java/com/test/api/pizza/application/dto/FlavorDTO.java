package com.test.api.pizza.application.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class FlavorDTO {
    @NotEmpty
    @Size(min = 5, max = 50)
    private String name;

    private Integer addonsId;
}
