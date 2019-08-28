<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
    <%@include file="/resources/css/terminals/display_barcodes.css" %>
    <%@include file="/resources/css/terminals/no_ptint.css" %>
</style>


<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Распечатать ШК</title>
</head>
<body>
<div class="back_button">
    <form action="${pageContext.servletContext.contextPath}/terminals">
        <button>Назад</button>
    </form>
</div>
<c:forEach items="${sessionScope.listOfTerminals}" var="eachTerminal" varStatus="theCount">

    <c:choose>
        <c:when test="${theCount.count==1 || (theCount.count - 1)%60==0}"> <!-- 1-й на странице -->
            <div class="page_frame">
            <table class="barcode_table">
            <tr class="barcode_row">
            <td class="barcode_cell">
                <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                     width="130" height="50">
                    ${eachTerminal.regId}
            </td>
        </c:when>

        <c:when test="${theCount.count %60==0}"> <!-- Последний на странице -->

            <td class="barcode_cell">
                <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                     width="130" height="50">
                    ${eachTerminal.regId}
            </td>
            </tr>
            </table>
            </div>
        </c:when>


        <c:when test="${(theCount.count - 1) %5==0}"> <!-- Первый в строке -->
            <tr class="barcode_row">
            <td class="barcode_cell">
                <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                     width="130" height="50">
                    ${eachTerminal.regId}
            </td>
        </c:when>

        <c:when test="${theCount.count %5==0}"> <!-- Последний в строке -->

            <td class="barcode_cell">
                <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                     width="130" height="50">
                    ${eachTerminal.regId}
            </td>
            </tr>
        </c:when>

        <c:otherwise>
            <td class="barcode_cell">
                <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                     width="130" height="50">
                    ${eachTerminal.regId}
            </td>
        </c:otherwise>
    </c:choose>
</c:forEach>
</body>
</html>

