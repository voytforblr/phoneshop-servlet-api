<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/priceHistories.css">
<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
    <h1>Order Overview</h1>
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
        <tags:orderOverviewRow name="firstName" label="First Name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" label="Last Name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery Date" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"></tags:orderOverviewRow>
        <tr>
            <td>
                Payment method
            </td>
            <td>
                    ${order.paymentMethod.name}
            </td>
        </tr>
    </table>
</tags:master>

