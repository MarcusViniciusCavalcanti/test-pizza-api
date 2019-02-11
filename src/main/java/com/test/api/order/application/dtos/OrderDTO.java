package com.test.api.order.application.dtos;

import com.test.api.order.application.validations.EnumValidator;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderDTO {

    @NotNull
    @EnumValidator(enumClazz = SizePizza.class, message = "Size selection does not exist")
    private String pizzaSize;

    @NotNull
    private Integer flavorId;

    private List<Integer> customizations;
}
