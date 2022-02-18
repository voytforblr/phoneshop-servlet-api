package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.history.DefaultHistoryService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private DefaultHistoryService historyService;
    private CartService cartService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        historyService = DefaultHistoryService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("history", historyService.getHistory(request));
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        String productIdString = request.getParameter("id");
        int index = findIndexInStringArray(productIds, productIdString);
        Long productId = Long.valueOf(productIdString);
        int quantity = 0;
        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = new HashMap<>();
        try {
            request.getLocale();
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantities[index]).intValue();
            if (quantity <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                cartService.add(cart, productId, quantity);
            }
        } catch (ParseException e) {
            errors.put(productId, "Not a number");
        } catch (OutOfStockException e) {
            errors.put(productId, "Out of stock, available " + e.getStockAvailable());
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?message=Product add to cart successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private int findIndexInStringArray(String[] stringArray, String findingString) {
        int i;
        for (i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equals(findingString)) {
                return i;
            }
        }
        return i;
    }
}





