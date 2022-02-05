<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/main.css">
<%--<jsp:useBean id="history" type="java.util.LinkedList" scope="request"/>--%>
<table>
    <c:if test="${not empty history}">
        <div>
            History
        </div>
    </c:if>
    <c:forEach var="historyItem" items="${history.history}">
        <td>
            <img class="historyImage" src="${historyItem.imageUrl}">
            <p></p>
            <a href="${pageContext.servletContext.contextPath}/products/${historyItem.id}">
                    ${historyItem.description}
            </a>
            <p></p>
            <fmt:formatNumber value="${historyItem.price}" type="currency"
                              currencySymbol="${historyItem.currency.symbol}"/>
        </td>
    </c:forEach>
</table>
