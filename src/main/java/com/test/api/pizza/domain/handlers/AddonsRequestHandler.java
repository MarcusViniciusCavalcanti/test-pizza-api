package com.test.api.pizza.domain.handlers;

import com.test.api.pizza.domain.entity.addons.Addons;

import java.rmi.NoSuchObjectException;


public abstract class AddonsRequestHandler {

    private AddonsRequestHandler next;

    AddonsRequestHandler(AddonsRequestHandler next) {
        this.next = next;
    }

    public <T extends Addons> T handleRequest(String value)  {
        if (next != null) {
            return next.handleRequest(value);
        }

        throw new RuntimeException(new NoSuchObjectException("Handler to addends type not implements"));
    }


}
