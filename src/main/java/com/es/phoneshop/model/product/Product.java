package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedList;
import java.util.Date;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private LinkedList<PriceHistory> priceHistories;

    public Product() {
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, LinkedList<PriceHistory> priceHistories) {
        this(null,code,description,price,currency,stock,imageUrl,priceHistories);
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl){
        this(null,code,description,price,currency,stock,imageUrl);
    }

    public Product(Long id,String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id=id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        LinkedList<PriceHistory> priceHistories=new LinkedList<>();
        priceHistories.add(new PriceHistory(price, new Date()));
        this.priceHistories=priceHistories;
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, LinkedList<PriceHistory> priceHistories) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistories=priceHistories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.priceHistories.add(new PriceHistory(price,new Date()));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LinkedList<PriceHistory> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(LinkedList<PriceHistory> priceHistories) {
        this.priceHistories = (LinkedList<PriceHistory>) priceHistories;
    }
}