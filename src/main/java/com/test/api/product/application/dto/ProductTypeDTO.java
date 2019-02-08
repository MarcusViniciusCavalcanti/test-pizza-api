package com.test.api.product.application.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductTypeDTO {
    @NotEmpty
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    private Integer timeProcess;
}