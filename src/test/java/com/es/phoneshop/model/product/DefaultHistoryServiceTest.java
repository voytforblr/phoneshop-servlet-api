package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.history.DefaultHistoryService;
import com.es.phoneshop.model.product.history.History;
import com.es.phoneshop.model.product.history.HistoryService;
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
public class DefaultHistoryServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;
    private ProductDao productDao;
    private HistoryService historyService;
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        historyService = DefaultHistoryService.getInstance();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testAddOneObjectTwoTimes() {
        Product product = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        History history = historyService.getHistory(request);
        int historySizeBeforeSecondAdd;
        int historySizeAfterSecondAdd;


        productDao.save(product);
        historyService.add(history, 0L);
        historySizeBeforeSecondAdd = historyService.getHistory(request).getHistory().size();
        historyService.add(history, 0L);
        historySizeAfterSecondAdd = historyService.getHistory(request).getHistory().size();

        assertEquals(historySizeAfterSecondAdd, historySizeBeforeSecondAdd);

    }

    @Test
    public void testAddFourObjects() {
        Product product1 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        Product product2 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        Product product3 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        Product product4 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        History history = historyService.getHistory(request);
        int historySize;

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        historyService.add(history, 0L);
        historyService.add(history, 1L);
        historyService.add(history, 2L);
        historyService.add(history, 3L);
        historySize = historyService.getHistory(request).getHistory().size();

        assertNotEquals(3, historySize);
    }

    @Test
    public void testReplaceSecondCell() {
        Product product0 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        Product product1 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        Product product2 = new Product("test", "sms", new BigDecimal(100), USD, 100, "image");
        History history = historyService.getHistory(request);

        productDao.save(product0);
        productDao.save(product1);
        productDao.save(product2);
        historyService.add(history, 0L);
        historyService.add(history, 1L);
        historyService.add(history, 2L);
        historyService.add(history, 1L);

        assertNotEquals(product1, historyService.getHistory(request).getHistory());
    }
}
