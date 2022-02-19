package com.es.phoneshop.model.product;

public class NotFoundException extends RuntimeException {
    protected Long id;

    public NotFoundException() {
    }

    public NotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
