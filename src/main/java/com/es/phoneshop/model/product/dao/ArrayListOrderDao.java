package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.NotFoundException;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {
    private ArrayListOrderDao() {
        itemList=new ArrayList<Order>();
    }

    private static class SingletonHolder {
        public static final ArrayListOrderDao HOLDER_INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public Order getOrder(Long id) {
        try {
            return super.getItem(id);
        } catch (NotFoundException e) {
            throw new OrderNotFoundException(e.getId());
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return itemList.stream().filter(order -> order.getSecureId().equals(id))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            lock.readLock().unlock();
        }
    }
}
