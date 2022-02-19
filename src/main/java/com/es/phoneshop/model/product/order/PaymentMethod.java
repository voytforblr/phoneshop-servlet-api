package com.es.phoneshop.model.product.order;

public enum PaymentMethod {
    CACHE("Cache"), CREDIT_CARD("Credit card");

    private final String name;

    PaymentMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
