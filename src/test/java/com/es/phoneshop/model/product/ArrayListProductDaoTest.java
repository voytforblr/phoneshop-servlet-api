package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNoFindException {

        Currency usd = Currency.getInstance("USD");
        Product product = (new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);

        assertTrue(product.getId() > 0);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test(expected = ProductNoFindException.class)
    public void testDeleteProduct() throws ProductNoFindException{

        Currency usd = Currency.getInstance("USD");
        Product product = (new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);
        productDao.delete(product.getId());

        Product result = productDao.getProduct(product.getId());
        assertNull(result);

    }

    @Test
    public void testSaveUpdateProduct() throws ProductNoFindException {
        Currency usd = Currency.getInstance("USD");
        Product product = (new Product("test-OldProduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);

        productDao.save(new Product((long) 0,"test-NewProduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        Product testProduct=productDao.getProduct((long) 0);
        assertNotEquals(product.getCode(), testProduct.getCode());
    }
}
