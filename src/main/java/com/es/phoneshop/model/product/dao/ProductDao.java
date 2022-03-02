package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends EntityDao<Product> {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void delete(Long id) throws ProductNotFoundException;

    List<Product> findProductsByAdvancedSearch(String productCode, BigDecimal minPrice, BigDecimal maxPrice, int minStock);

}
