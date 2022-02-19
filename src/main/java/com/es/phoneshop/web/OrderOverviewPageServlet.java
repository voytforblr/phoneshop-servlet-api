package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListOrderDao;
import com.es.phoneshop.model.product.dao.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderDao orderDao;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderSecureId = request.getPathInfo().substring(1);
        request.setAttribute("order", orderDao.getOrderBySecureId(orderSecureId));
        request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
    }

}
