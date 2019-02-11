package com.test.api.pizza.domain.valueObject.size;

public class Middle implements Size {
    @Override
    public Double value() {
        return 30.0;
    }

    @Override
    public Integer timeProcess() {
        return 20;
    }
}
