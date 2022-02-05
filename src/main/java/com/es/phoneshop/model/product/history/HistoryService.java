package com.es.phoneshop.model.product.history;

import javax.servlet.http.HttpServletRequest;

public interface HistoryService {
    History getHistory(HttpServletRequest request);

    void add(History history, Long id);
}
