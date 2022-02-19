package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.NotFoundException;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao extends AbstractDao implements OrderDao {
    private ArrayListOrderDao() {
        orderList = new ArrayList<>();
        super.itemList.addAll(orderList);
    }

    private static class SingletonHolder {
        public static final ArrayListOrderDao HOLDER_INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private List<Order> orderList;

    //здесь не удалось сильно уменьшить повтор кода, так как для save требуется сохранение в 2 списка
    //и выходит так, что необходимо делать lock на оба сохранения
    @Override
    public void save(Order order) {
        int indexToInsert = super.save(order);
        lock.readLock().lock();
        try {
            if (indexToInsert < orderList.size()) {
                orderList.set(indexToInsert, order);
            } else {
                orderList.add(order);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Order getOrder(Long id) {
        try {
            return (Order) super.getItem(id);
        } catch (NotFoundException e) {
            throw new OrderNotFoundException(e.getId());
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return orderList.stream().filter(order -> order.getSecureId().equals(id))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            lock.readLock().unlock();
        }
    }
}
