package com.es.phoneshop.model.product;

import java.io.Serializable;

public class Entity implements Serializable {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected Long id;
}
