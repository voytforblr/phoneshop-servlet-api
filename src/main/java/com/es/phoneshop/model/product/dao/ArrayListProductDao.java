package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.NotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {
    private ArrayListProductDao() {
        itemList = new ArrayList<Product>();
    }

    private static class SingletonHolder {
        public static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        try {
            return super.getItem(id);
        } catch (NotFoundException e) {
            throw new ProductNotFoundException(e.getId());
        }
    }

    @Override
    public List<Product> findProductsByAdvancedSearch(String productCode, BigDecimal minPrice, BigDecimal maxPrice, int minStock) {
        lock.readLock().lock();
        try {
            return itemList.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                    .filter(product -> product.getStock() >= minStock)
                    //здесь сделан поиск по любому совпадению, если было введено несколько слов,
                    //будет совершен поиск по всем словам
                    //для поиска по точному совпадению необходимо anyMatch() заменить на allMatch()
                    .filter(product -> Arrays.stream(productCode.trim().replaceAll("\\s+", " ").split("\\s"))
                            .anyMatch(word -> isWordInString(word, product.getCode()))
                    )
                    //сортировка по релевантности по коду продукта
                    .sorted((product1, product2) -> compareByRelevance(product1.getCode(), product2.getCode(), productCode))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    //этот метод был написан на первой лекции и я уже не успеваю его поправить и ничего не сломать (
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
            //все фильтры можно отнести в один стрим, если query определить как пустую строку в сервлете
            //так же можно можно вызывать sorted на стриме если он не null через тернарный оператор
            //тем самым избавиться от всех if
            if (query != null && sortOrder != null) {
                return itemList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                .anyMatch(word -> product.getDescription().toLowerCase().contains(word.toLowerCase())))
                        .sorted((SortOrder.ASC == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            }
            if (query == null || query.isEmpty()) {
                return itemList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .sorted((SortOrder.ASC == sortOrder) ? comparatorField : comparatorField.reversed())
                        .collect(Collectors.toList());
            } else {// if (query!=null && sortOrder==null)
                return itemList.stream()
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> Arrays.stream(query.split("\\s"))
                                .anyMatch(word -> isWordInString(word, product.getDescription())))
                        .sorted((product1, product2) ->
                                compareByRelevance(product1.getDescription(), product2.getDescription(), query))
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
            Product findProduct = itemList.stream().filter(product1 ->
                    product1.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
            itemList.remove(findProduct);
            itemList.remove(findProduct);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isWordInString(String word, String string) {
        return string.toLowerCase().contains(word.toLowerCase());
    }

    private int compareByRelevance(String item1, String item2, String query) {
        String[] queryArray = query.trim().replaceAll("\\s+", " ").split("\\s");
        return (int) (Arrays.stream(queryArray)
                .filter(word -> isWordInString(word, item2))
                .count()
                - Arrays.stream(queryArray)
                .filter(word -> isWordInString(word, item1))
                .count());
    }
}

