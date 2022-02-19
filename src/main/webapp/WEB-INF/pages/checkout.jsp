<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/priceHistories.css">
<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an while placing order
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td class="quantity">
                    Quantity
                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${order.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td class="price">
                        <div class="dropdown">
                            <a href="#" class="dropbtn">
                                <fmt:formatNumber value="${item.product.price}" type="currency"
                                                  currencySymbol="${item.product.currency.symbol}"/>
                            </a>
                        </div>
                        <div id="tooltip"></div>
                    </td>
                    <td>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                            ${item.quantity}
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    Total quantity:
                    <p>
                        <fmt:formatNumber value="${order.totalQuantity}"/>
                    </p>
                </td>
                <td>
                    Total delivery cost:
                    <p>
                        <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                          currencySymbol="${order.items[0].product.currency.symbol}"/>
                    </p>
                </td>
                <td>
                    Subtotal:
                    <p>
                        <fmt:formatNumber value="${order.subtotal}" type="currency"
                                          currencySymbol="${order.items[0].product.currency.symbol}"/>
                    </p>
                </td>
                <td>
                    Total cost:
                    <p>
                        <fmt:formatNumber value="${order.totalCost}" type="currency"
                                          currencySymbol="${order.items[0].product.currency.symbol}"/>
                    </p>
                </td>
            </tr>
        </table>
        <h2>Your Details</h2>
        <table>
            <tags:orderFormRow name="firstName" label="First Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryDate" label="Delivery Date" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>
                    Payment Method
                </td>
                <td>
                    <select name="paymentMethod">
                        <c:forEach var="method" items="${paymentMethods}">
                            <option value="${method.name()}">${method.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <p>
            <button>Place order</button>
        </p>
    </form>
</tags:master>

