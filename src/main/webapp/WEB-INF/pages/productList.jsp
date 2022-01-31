<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="../../styles/priceHistories.css">
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">

    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc" symbol="&#8679"/>
                <tags:sortLink sort="description" order="desc" symbol="&#8681"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc" symbol="&#8679"/>
                <tags:sortLink sort="price" order="desc" symbol="&#8681"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
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
                                           <%-- <fmt:formatDate value="${history.date}" pattern="yyyy-MM-dd"/>--%>
                                            ${history.date}
                                        </c:forEach>
                                    </div>
                                </div>

                            </h3>
                        </div>
                    </div>
                    <div id="tooltip"></div>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>

