package com.test.api.utils;

import java.util.List;
import java.util.Map;

public abstract class FactoryObjectsToTest<T> {

    abstract protected T createSample();
    abstract protected  List<T> createList();
    abstract protected T createBy(Map<String, Object> args);

    public T createOneObject() {
        return createSample();
    }

    public List<T> createListObjects() {
        return createList();
    }

    public T createObjectBy(Map<String, Object> args) {
        return createBy(args);
    }
}
