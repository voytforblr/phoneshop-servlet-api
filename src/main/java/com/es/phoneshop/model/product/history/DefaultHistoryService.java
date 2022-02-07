package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.Cart;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DefaultHistoryService implements HistoryService {

    private static final String HISTORY_SESSION_ATTRIBUTE = DefaultHistoryService.class + ".history";
    private ProductDao productDao;
    private final int HISTORY_SIZE = 3;

    private DefaultHistoryService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHolder {
        public static final DefaultHistoryService HOLDER_INSTANCE = new DefaultHistoryService();
    }

    public static DefaultHistoryService getInstance() {
        return DefaultHistoryService.SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public synchronized History getHistory(HttpServletRequest request) {
        History history = (History) request.getSession().getAttribute(HISTORY_SESSION_ATTRIBUTE);
        if (history == null) {
            request.getSession().setAttribute(HISTORY_SESSION_ATTRIBUTE, history = new History());
        }
        return history;
    }

    @Override
    public synchronized void add(History history, Long id) {
        if (history.getHistory().contains(productDao.getProduct(id))) {
            history.getHistory().remove(productDao.getProduct(id));
            history.getHistory().add(0, productDao.getProduct(id));
            return;
        }
        if (history.getHistory().size() == HISTORY_SIZE) {
            history.getHistory().remove(HISTORY_SIZE - 1);
            history.getHistory().add(0, productDao.getProduct(id));
            return;
        }
        history.getHistory().add(0, productDao.getProduct(id));
    }
}
