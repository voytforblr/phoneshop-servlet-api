package com.es.phoneshop.model.product.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart, Long id, int quantity) throws OutOfStockException;

    void update(Cart cart, Long id, int quantity) throws OutOfStockException;

    void delete(Cart cart, Long id);

}
