package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.dao.ArrayListOrderDao;
import com.es.phoneshop.model.product.dao.OrderDao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    OrderDao orderDao;
    CartService cartService;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    private static class SingletonHolder {
        public static final DefaultOrderService HOLDER_INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        order.setTotalQuantity(cart.getTotalQuantity());
        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order, Cart cart) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        cartService.updateProductsQuantities(cart);
        cartService.clearCart(cart);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }


}
