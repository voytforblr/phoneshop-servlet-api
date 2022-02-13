package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
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
public class ProductListPageServletTest {
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
    private ProductListPageServlet servlet = new ProductListPageServlet();
    private Locale locale = Locale.getDefault();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        Product product = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        productDao.save(product);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(locale);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("history"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        String[] quantityVal = {"eee", "5", "-1", "10000"};
        String[] productIdVal = {"0", "1", "2", "3"};
        when(request.getParameterValues("quantity")).thenReturn(quantityVal);
        when(request.getParameterValues("productId")).thenReturn(productIdVal);
        when(request.getParameter("id")).thenReturn("0").thenReturn("1").thenReturn("2")
                .thenReturn("3");

        servlet.doPost(request, response);

        verify(servlet).doGet(request, response);
    }


}