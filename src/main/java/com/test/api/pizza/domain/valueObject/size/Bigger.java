package com.test.api.pizza.domain.valueObject.size;

public class Bigger implements Size {
    @Override
    public Double value() {
        return 40.0;
    }

    @Override
    public Integer timeProcess() {
        return 25;
    }
}
