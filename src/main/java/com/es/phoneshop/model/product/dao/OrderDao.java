package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderNotFoundException;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    Order getOrderBySecureId(String id) throws OrderNotFoundException;

    void save(Order order) throws OrderNotFoundException;

}
