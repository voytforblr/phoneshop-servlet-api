package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductDao productDao;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCode = request.getParameter("productCode");
        String minPriceString = request.getParameter("minPrice");
        String maxPriceString = request.getParameter("maxPrice");
        String minStock = request.getParameter("minStock");

        Map<String, String> errors = new HashMap<>();
        productCode = productCode == null ? "" : productCode;
        BigDecimal minPrice = parsePrice(minPriceString, "minPrice", errors, request);
        BigDecimal maxPrice = parsePrice(maxPriceString, "maxPrice", errors, request);
        if (maxPrice != null && minPrice != null && maxPrice.compareTo(minPrice) < 0) {
            errors.put("maxPrice", "Max price must be larger than Min price");
        }
        int stock = parseStock(minStock, errors, request);

        request.setAttribute("errors", errors);
        request.setAttribute("products",
                productDao.findProductsByAdvancedSearch(productCode, minPrice, maxPrice, stock));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private BigDecimal parsePrice(String priceString, String name, Map<String, String> errors, HttpServletRequest request) {
        if (priceString == null || priceString.isEmpty()) {
            return null;
        }
        double price = 0;
        try {
            request.getLocale();
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            price = format.parse(priceString).doubleValue();
            return BigDecimal.valueOf(price);
        } catch (ParseException e) {
            errors.put(name, "Incorrect number");
            return null;
        }
    }

    private int parseStock(String stockString, Map<String, String> errors, HttpServletRequest request) {
        if (stockString == null || stockString.isEmpty()) {
            return 0;
        }
        int stock = 0;
        try {
            request.getLocale();
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            stock = format.parse(stockString).intValue();
            return stock;
        } catch (ParseException e) {
            errors.put("minStock", "Incorrect number");
            return 0;
        }
    }

}





