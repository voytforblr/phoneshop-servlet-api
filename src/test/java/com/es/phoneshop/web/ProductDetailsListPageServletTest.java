package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.history.DefaultHistoryService;
import com.es.phoneshop.model.product.history.HistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    @Spy
    private ProductDetailsListPageServlet servlet = new ProductDetailsListPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private CartService cartService = DefaultCartService.getInstance();
    private HistoryService historyService = DefaultHistoryService.getInstance();
    private Locale locale = Locale.getDefault();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);
        productDao.save(new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "image"));
        Cart cart = new Cart();
        cartService.add(cart, 0L, 10);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(locale);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getContextPath()).thenReturn("/phoneshop-servlet-api");

    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("history"), any());
    }

    @Test
    public void doPost() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");
        servlet.doPost(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}