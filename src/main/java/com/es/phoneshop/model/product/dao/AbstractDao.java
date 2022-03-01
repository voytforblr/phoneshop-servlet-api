package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Entity;
import com.es.phoneshop.model.product.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractDao<T extends Entity> implements EntityDao<T> {
    protected List<T> itemList;
    protected long maxId;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public T getItem(Long id) {
        lock.readLock().lock();
        try {
            return itemList.stream().filter(item -> item.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(T item) {
        lock.writeLock().lock();
        try {
            if (item.getId() != null) {
                long id = item.getId();
                T findOrder = getItem(id);
                int index = itemList.indexOf(findOrder);
                itemList.set(index, item);
            } else {
                item.setId(maxId++);
                itemList.add(item);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
