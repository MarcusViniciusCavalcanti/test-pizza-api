package com.test.api.pizza.application.dto;

import com.test.api.pizza.application.validations.AddonsValueValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Data
public class AddonsDTO {
    @NotEmpty
    @Size(min = 5, max = 50)
    private String name;

    @AddonsValueValidator
    private String value;
}
