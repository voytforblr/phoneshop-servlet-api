package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProducts() throws ProductNotFoundException {
        int oldLengthOfFindProductsArray=productDao.findProducts().size();

        productDao.save(new Product("price not null and stock>0","test1", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("price null and stock>0","test2", null, USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("price not null and stock<=0","test3", new BigDecimal(100), USD, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("price null and stock<=0","test4", null, USD, -1, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        int newLengthOfFindProductsArray=productDao.findProducts().size();
        assertNotEquals(oldLengthOfFindProductsArray, newLengthOfFindProductsArray);

    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {

        Product product = (new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);

        assertTrue(product.getId() > 0);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException {

        Currency usd = Currency.getInstance("USD");
        Product product = (new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);
        productDao.delete(product.getId());

        productDao.getProduct(product.getId());

    }

    @Test
    public void testSaveUpdateProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = (new Product("test-OldProduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);

        productDao.save(new Product(0L, "test-NewProduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        Product testProduct = productDao.getProduct(0L);
        assertNotEquals(product.getCode(), testProduct.getCode());
    }
}
