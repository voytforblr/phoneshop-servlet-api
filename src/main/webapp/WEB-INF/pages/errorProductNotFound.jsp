<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product not found">
    <h1>
            <%--первый вариант id через pageContext.errorData.requestURI--%>
            <%--Product with code ${pageContext.errorData.requestURI
            .substring("/phoneshop-servlet-api/products/".length())} not found--%>
            <%--выводит Product with code 31241241 not found--%>
            <%--но этот вариант не очень из-за статического указания URI--%>
            <%--с lastIndexOf() так и не заработало--%>
        Product with code ${pageContext.exception.getId()} not found
    </h1>
</tags:master>