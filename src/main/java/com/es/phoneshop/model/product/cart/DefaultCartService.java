package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHolder {
        public static final DefaultCartService HOLDER_INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long id, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(id);
        CartItem item = findCartItem(cart, product);
        if (item == null) {
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getItems().add(new CartItem(product, quantity));
        } else {
            if (product.getStock() < quantity + item.getQuantity()) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            item.setQuantity(quantity + item.getQuantity());
        }
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long id, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(id);
        CartItem item = findCartItem(cart, product);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        item.setQuantity(quantity);
        recalculateCart(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long id) {
        cart.getItems().removeIf(cartItem ->
                id.equals(cartItem.getProduct().getId()));
        recalculateCart(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.setTotalCost(BigDecimal.ZERO);
        cart.setTotalQuantity(0);
        cart.getItems().clear();
    }

    @Override
    public void updateProductsQuantities(Cart cart) {
        cart.getItems().forEach(cartItem ->
                cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity()));
    }

    private CartItem findCartItem(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findAny()
                .orElse(null);
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(q -> q)
                .sum());
        cart.setTotalCost(cart.getItems().stream()
                .map(this::calculateRow)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal calculateRow(CartItem cartItem) {
        return cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }
}
