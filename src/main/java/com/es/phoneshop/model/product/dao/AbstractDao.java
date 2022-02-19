package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Entity;
import com.es.phoneshop.model.product.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractDao {
    protected List<Entity> itemList = new ArrayList<>();
    protected long maxId;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    //плохая реализация, так как у нас по факту имеется 2 списка
    public Entity getItem(Long id) {
        lock.readLock().lock();
        try {
            return itemList.stream().filter(item -> item.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    public int save(Entity item) {
        lock.writeLock().lock();
        try {
            if (item.getId() != null) {
                long id = item.getId();
                Entity findOrder = getItem(id);
                int index = itemList.indexOf(findOrder);
                itemList.set(index, item);
                return index;
            } else {
                item.setId(maxId++);
                itemList.add(item);
                return itemList.size() - 1;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
