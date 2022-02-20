package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Entity;
import com.es.phoneshop.model.product.NotFoundException;

public interface EntityDao<T extends Entity> {
    T getItem(Long id) throws NotFoundException;

    void save(T item);

}
