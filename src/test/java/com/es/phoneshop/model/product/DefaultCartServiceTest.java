package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Currency;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    private ProductDao productDao;
    private CartService cartService;

    private final Currency USD = Currency.getInstance("USD");


    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        when(request.getSession()).thenReturn(session);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductToCartWithoutEnoughStock() throws OutOfStockException {
        Cart cart = new Cart();
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");

        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 120);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductToCartWithoutEnoughStockWithSum() throws OutOfStockException {
        Cart cart = new Cart();
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        cartService.add(cart, 0L, 60);
    }

    @Test
    public void testGetCart() throws OutOfStockException {
        Cart cart = new Cart();
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        Product productWithStock80 = new Product("code", "sms", new BigDecimal(100), USD, 80, "image");

        productDao.save(productWithStock80);
        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        cartService.add(cart, 1L, 60);
        Cart cartForSize = cartService.getCart(request);

        assertNotEquals(cartForSize.getItems().size(), 2);
    }
}
