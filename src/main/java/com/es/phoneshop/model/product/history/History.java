package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.Product;

import java.util.LinkedList;
import java.util.List;

public class History {

    private List<Product> history;

    public History() {
        this.history = new LinkedList<>();
    }

    public List<Product> getHistory() {
        return history;
    }

    @Override
    public String toString() {
        return "History[ " + history + " ]";
    }
}
