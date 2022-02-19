package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product) throws ProductNotFoundException;

    void delete(Long id) throws ProductNotFoundException;
}
