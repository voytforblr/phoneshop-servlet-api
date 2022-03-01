package com.es.phoneshop.model.product;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(Long id) {
        super(id);
    }
}
