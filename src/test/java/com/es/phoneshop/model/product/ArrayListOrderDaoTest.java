package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.ArrayListOrderDao;
import com.es.phoneshop.model.product.dao.OrderDao;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Before
    public void setup() throws OrderNotFoundException {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderWithUnrealId() {
        long id = 1000L;

        Order order = orderDao.getOrder(id);
    }

    @Test
    public void testGetOrder() {
        Order result;
        Order order = new Order();
        order.setSecureId("123");

        orderDao.save(order);
        result = orderDao.getOrder(order.getId());

        assertEquals(order, result);
    }

    @Test
    public void testGetOrderBySecureId() {
        Order result;
        Order order = new Order();
        order.setSecureId("123");

        orderDao.save(order);
        result = orderDao.getOrderBySecureId("123");

        assertNotEquals(order, result);
    }

    @Test
    public void testSaveNewOrder() throws OrderNotFoundException {
        Order order = new Order();
        order.setSecureId("123");
        Order result;

        orderDao.save(order);
        result = orderDao.getOrder(order.getId());

        assertEquals(0, (long) order.getId());
        assertNotNull(result);
        assertEquals("123", result.getSecureId());
    }


    @Test
    public void testSaveUpdateOrder() throws OrderNotFoundException {
        Order orderWithoutId = new Order();
        orderWithoutId.setSecureId("Order without id");
        Order orderWithId = new Order();
        orderWithId.setId(0L);
        orderWithId.setSecureId("Order with id");

        orderDao.save(orderWithoutId);
        orderDao.save(orderWithId);
        Order testOrder = orderDao.getOrder(0L);

        System.out.println(testOrder.getSecureId());
        assertNotEquals(orderWithoutId.getSecureId(), testOrder.getSecureId());
    }
}
