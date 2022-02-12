<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/priceHistories.css">
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an error adding to cart
        </div>
    </c:if>
    <form id="addCartItem" method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                    <tags:sortLink sort="DESCRIPTION" order="ASC" symbol="&#8679"/>
                    <tags:sortLink sort="DESCRIPTION" order="DESC" symbol="&#8681"/>
                </td>
                <td class="price">
                    Price
                    <tags:sortLink sort="PRICE" order="ASC" symbol="&#8679"/>
                    <tags:sortLink sort="PRICE" order="DESC" symbol="&#8681"/>
                </td>
                <td>
                    Quantity
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td class="price">

                        <div class="dropdown">
                            <a href="#" class="dropbtn">
                                <fmt:formatNumber value="${product.price}" type="currency"
                                                  currencySymbol="${product.currency.symbol}"/>
                            </a>
                            <div class="dropdown-content">
                                <h1>
                                    Price history
                                </h1>
                                <h2>
                                        ${product.description}
                                </h2>
                                <h3>
                                    <div class="container">
                                        <div class="box1">Price
                                            <p></p>
                                            <c:forEach var="history" items="${product.priceHistories}">

                                                <fmt:formatNumber value="${history.price}" type="currency"
                                                                  currencySymbol="${product.currency.symbol}"/>
                                            </c:forEach>
                                        </div>
                                        <div class="box2">Date
                                            <p></p>
                                            <c:forEach var="history" items="${product.priceHistories}">
                                                ${history.date}
                                            </c:forEach>
                                        </div>
                                    </div>

                                </h3>
                            </div>
                        </div>
                        <div id="tooltip"></div>
                    </td>
                    <td>
                        <c:set var="error" value="${errors[product.id]}"/>
                        <input class="quantity" name="quantity"
                               value="${not empty errors ? param.quantity : 1}">

                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                        <input type="hidden" name="productId" value="${product.id}">
                    </td>
                    <td>
                        <button form="addCartItem"
                                formaction="${pageContext.servletContext.contextPath}/products?id=${product.id}">
                            Add to cart
                        </button>
                    </td>

                </tr>
            </c:forEach>
        </table>
    </form>
    <jsp:include page="history.jsp"/>
</tags:master>

