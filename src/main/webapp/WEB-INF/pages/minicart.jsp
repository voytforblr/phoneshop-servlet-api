<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/priceHistories.css">
<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="request"/>
Cart:${cart.totalQuantity} items
<c:if test="${not empty cart.items}">
    <fmt:formatNumber value="${cart.totalCost}" type="currency"
                      currencySymbol="${cart.items[0].product.currency.symbol}"/>,total sum
</c:if>

