<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <p>
            Product code
        </p>
        <input name="productCode" value="${param.productCode}">
        <p>
            Min price
        </p>
        <input name="minPrice" value="${param.minPrice}">
        <c:if test="${not empty errors['minPrice']}">
            <div class="error">
                    ${errors['minPrice']}
            </div>
        </c:if>
        <p>
            Max price
        </p>
        <input name="maxPrice" value="${param.maxPrice}">
        <c:if test="${not empty errors['maxPrice']}">
            <div class="error">
                    ${errors['maxPrice']}
            </div>
        </c:if>
        <p>
            Min stock
        </p>
        <input name="minStock" value="${param.minStock}">
        <c:if test="${not empty errors['minStock']}">
            <div class="error">
                    ${errors['minStock']}
            </div>
        </c:if>
        <p>
            <button>Search</button>
        </p>
    </form>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an error in search
        </div>
    </c:if>
    <c:if test="${empty errors and products.size()<=0}">
        Sorry, we didn't find any products matching your request :-(
    </c:if>
    <c:if test="${empty errors and products.size()>0}">
        <div class="success">
            Found ${products.size()} products
        </div>
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
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                        <div id="tooltip"></div>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

</tags:master>

