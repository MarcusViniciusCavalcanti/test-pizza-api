package com.test.api.pizza.domain.handlers;

import com.test.api.pizza.domain.entity.addons.Addons;

public class TypeAddendHandler {

    private AddonsRequestHandler chain;

    public TypeAddendHandler() {
        buildChain();
    }

    private void buildChain() {
        chain = new AddendsTimeProcess(new AddendsPrice(null));
    }

    public <T extends Addons> T getAddendsByValueType(String value) {
        return chain.handleRequest(value);
    }
}
