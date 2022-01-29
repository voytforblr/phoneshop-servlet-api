package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductWithUnrealId() {
        //1.
        long id = 1000L;

        //2.
        productDao.getProduct(id);

        //3.
    }

    @Test
    public void testGetProduct(){
        //1.
        Product product=new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "image");
        Product result;

        //2.
        productDao.save(product);
        result=productDao.getProduct(0L);

        //3.
        assertEquals(product,result);
    }

    @Test
    public void testFindProducts() throws ProductNotFoundException {
        //1.
        int oldLengthOfFindProductsArray = productDao.findProducts(null, null, null).size();
        Product testCorrectProduct1 = new Product("price not null and stock>0", "test1", new BigDecimal(100), USD, 100, "image");
        Product testCorrectProduct2 = new Product("price not null and stock>0", "test2", new BigDecimal(100), USD, 100, "image");
        Product testUnCorrectProduct3 = new Product("price not null and stock<=0", "test3", new BigDecimal(100), USD, 0, "image");
        Product testUnCorrectProduct4 = new Product("price null and stock<=0", "test4", null, USD, -1, "image");
        int newLengthOfFindProductsArrayAfterTest1;
        int newLengthOfFindProductsArrayAfterTest2;
        int newLengthOfFindProductsArrayAfterTest3;
        int newLengthOfFindProductsArrayAfterTest4;

        //2.
        productDao.save(testCorrectProduct1);
        newLengthOfFindProductsArrayAfterTest1 = productDao.findProducts(null, null, null).size();
        productDao.save(testCorrectProduct2);
        newLengthOfFindProductsArrayAfterTest2 = productDao.findProducts(null, null, null).size();
        productDao.save(testUnCorrectProduct3);
        newLengthOfFindProductsArrayAfterTest3 = productDao.findProducts(null, null, null).size();
        productDao.save(testUnCorrectProduct4);
        newLengthOfFindProductsArrayAfterTest4 = productDao.findProducts(null, null, null).size();

        //3.
        assertNotEquals(oldLengthOfFindProductsArray, newLengthOfFindProductsArrayAfterTest1);
        assertNotEquals(newLengthOfFindProductsArrayAfterTest1, newLengthOfFindProductsArrayAfterTest2);
        assertEquals(newLengthOfFindProductsArrayAfterTest2, newLengthOfFindProductsArrayAfterTest3);
        assertEquals(newLengthOfFindProductsArrayAfterTest3, newLengthOfFindProductsArrayAfterTest4);


    }

    @Test
    public void testFindProductsQueryAndSortOrder(){
        //1.
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "image"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD, 5, "image"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(200), USD, 10, "image"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(200), USD, 10, "image"));

        //2.

        //3.
        assertTrue(productDao.findProducts(null, null, null).get(0).getStock()>0);
        assertNotNull(productDao.findProducts(null, null, null).get(0).getPrice());

        assertTrue(productDao.findProducts("Sam", null, null).get(0).getStock()>0);
        assertNotNull(productDao.findProducts("Sam", null, null).get(0).getPrice());

        assertTrue(productDao.findProducts("Sam", null, SortOrder.asc).get(0).getStock()>0);
        assertNotNull(productDao.findProducts("Sam", null, SortOrder.asc).get(0).getPrice());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        //1.
        Product product = (new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "image"));

        //2.
        productDao.save(product);
        Product result = productDao.getProduct(product.getId());

        //3.
        assertTrue(product.getId() > 0);
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException {
        //1.
        Product product = (new Product("test-product", "phone", new BigDecimal(100), USD, 100, "image"));

        //2.
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.getProduct(product.getId());

        //3.

    }

    @Test
    public void testSaveUpdateProduct() throws ProductNotFoundException {
        //1.
        Product productWithoutId = (new Product("test-OldProduct", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "image"));
        Product productWithId=new Product(0L, "test-NewProduct", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "image");

        //2.
        productDao.save(productWithoutId);
        productDao.save(productWithId);
        Product testProduct = productDao.getProduct(0L);

        //3.
        assertNotEquals(productWithoutId.getCode(), testProduct.getCode());
    }
}
