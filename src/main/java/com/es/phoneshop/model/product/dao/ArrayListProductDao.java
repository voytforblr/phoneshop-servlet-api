package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.NotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDao implements ProductDao {
    private ArrayListProductDao() {
        productList = new ArrayList<>();
        itemList.addAll(productList);
    }

    private static class SingletonHolder {
        public static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private List<Product> productList;

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        try {
            return (Product) super.getItem(id);
        } catch (NotFoundException e) {
            throw new ProductNotFoundException(e.getId());
        }
    }

    @Override
    public void save(Product product) throws ProductNotFoundException {
        int indexToInsert = super.save(product);
        lock.writeLock().lock();
        try {
            if (indexToInsert < productList.size()) {
                productList.set(indexToInsert, product);
            } else {
                productList.add(product);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparatorField = Comparator.comparing(product -> {
                if (SortField.DESCRIPTION == sortField) {
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });
            if (query == null && sortField == null && sortOrder == null) {
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .collect(Collectors.toList());
            }
            if (query != null && sortOrder != null) {
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                .anyMatch(word -> isWordInDescription(product, word)))
                        .sorted((SortOrder.ASC == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            }
            if (query == null || query.isEmpty()) {
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .sorted((SortOrder.ASC == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            } else {// if (query!=null && sortOrder==null)
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                .anyMatch(word -> isWordInDescription(product, word)))
                        .sorted((product1, product2) ->
                                (int) (Arrays.stream(query.split("\\s"))
                                        .filter(word -> isWordInDescription(product2, word))
                                        .count()
                                        - Arrays.stream(query.split("\\s"))
                                        .filter(word -> isWordInDescription(product1, word))
                                        .count()))
                        .collect(Collectors.toList());
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        lock.writeLock().lock();
        try {
            Product findProduct = productList.stream().filter(product1 ->
                    product1.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
            productList.remove(findProduct);
            itemList.remove(findProduct);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isWordInDescription(Product product, String word) {
        return product.getDescription().toLowerCase().contains(word.toLowerCase());
    }
}

