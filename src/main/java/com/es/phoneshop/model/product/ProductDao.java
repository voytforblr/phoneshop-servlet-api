package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNoFindException;
    List<Product> findProducts();
    void save(Product product);
    void delete(Long id) throws ProductNoFindException;
}
