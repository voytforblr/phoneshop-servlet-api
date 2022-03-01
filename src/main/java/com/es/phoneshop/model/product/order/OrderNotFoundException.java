package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.NotFoundException;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(Long id) {
        super(id);
    }

    public OrderNotFoundException() {

    }
}
