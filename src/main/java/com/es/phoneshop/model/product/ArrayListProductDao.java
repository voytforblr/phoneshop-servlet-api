package com.es.phoneshop.model.product;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private ArrayListProductDao() {
        productList = new ArrayList<>();
    }

    private static class SingletonHolder {
        public static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private List<Product> productList;
    private long maxId;
    private ReadWriteLock lock = new ReentrantReadWriteLock();


    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return productList.stream().filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparatorField = Comparator.comparing(product -> {
                if (SortField.description == sortField) {
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });
            if (query != null && sortOrder != null) {
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                .anyMatch(word -> product.getDescription().toLowerCase().contains(word.toLowerCase())))
                        .sorted((SortOrder.asc == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            }
            if (query == null || query.isEmpty()) {
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .sorted((SortOrder.asc == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            } else {// if (query!=null && sortOrder==null)
                return productList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                //anyMatch, так как если использовать allMatch оно находит все совпадения
                                //и не видны все результаты вхождения, например, если ввести запрос
                                //"Samsung S", то anyMatch вернет так же и вхождения "S", в
                                //отличие от примера allMatch, вернет только полное вхождение фразы
                                //"Samsung S", но я если бы не пример я бы оставил allMatch
                                .anyMatch(word -> product.getDescription().toLowerCase().contains(word.toLowerCase())))
                        .sorted((product1, product2) ->
                                (int) (Arrays.stream(query.split("\\s"))
                                        .filter(word -> product2.getDescription().toLowerCase().contains(word.toLowerCase()))
                                        .count()
                                        - Arrays.stream(query.split("\\s"))
                                        .filter(word -> product1.getDescription().toLowerCase().contains(word.toLowerCase()))
                                        .count()))
                        .collect(Collectors.toList());
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) throws ProductNotFoundException {
        lock.writeLock().lock();
        try {
            if (product.getId() != null) {
                long id = product.getId();
                Product findProduct = productList.stream().filter(product1 ->
                        product1.getId().equals(id))
                        .findAny()
                        .orElseThrow(() -> new ProductNotFoundException(id));
                productList.set(productList.indexOf(findProduct), product);
            } else {
                product.setId(maxId++);
                productList.add(product);
            }
        } finally {
            lock.writeLock().unlock();
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
        } finally {
            lock.writeLock().unlock();
        }
    }
}
