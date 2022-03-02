package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
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
    private Cart cart;
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
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
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        Product productWithStock80 = new Product("code", "sms", new BigDecimal(100), USD, 80, "image");

        productDao.save(productWithStock80);
        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        cartService.add(cart, 1L, 60);

        assertEquals(cart.getItems().size(), 2);
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateWithoutEnoughStock() throws OutOfStockException {
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");

        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        cartService.update(cart, 0L, 120);
    }

    @Test
    public void testUpdateWithCorrectStock() throws OutOfStockException {
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        int totalQuantityBeforeUpdate;

        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        totalQuantityBeforeUpdate = cart.getTotalQuantity();
        cartService.update(cart, 0L, 50);

        assertNotEquals(totalQuantityBeforeUpdate, cart.getTotalQuantity());
    }

    @Test
    public void testDelete() throws OutOfStockException {
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        int totalQuantityBeforeDelete;

        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        totalQuantityBeforeDelete = cart.getTotalQuantity();
        cartService.delete(cart, 0L);

        assertNotEquals(totalQuantityBeforeDelete, cart.getTotalQuantity());
    }

    @Test
    public void testClear() throws OutOfStockException {
        Product productWithStock100 = new Product("code", "sms", new BigDecimal(100), USD, 100, "image");
        int totalQuantityAfterClear;
        BigDecimal totalCostAfterClear;
        int totalCartItemSize;

        productDao.save(productWithStock100);
        cartService.add(cart, 0L, 60);
        //cartService.clear(cart);
        totalQuantityAfterClear = cart.getTotalQuantity();
        totalCostAfterClear = cart.getTotalCost();
        totalCartItemSize = cart.getItems().size();


        assertEquals(0, totalQuantityAfterClear);
        assertEquals(BigDecimal.ZERO, totalCostAfterClear);
        assertEquals(0, totalCartItemSize);
    }
}
