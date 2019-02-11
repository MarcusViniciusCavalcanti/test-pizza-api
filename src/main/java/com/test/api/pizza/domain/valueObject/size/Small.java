package com.test.api.pizza.domain.valueObject.size;

public class Small implements Size {

    @Override
    public Double value() {
        return 20.0;
    }

    @Override
    public Integer timeProcess() {
        return 15;
    }
}
