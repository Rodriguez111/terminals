<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
    <%@include file="/resources/css/terminals/display_barcodes.css" %>
    <%@include file="/resources/css/terminals/no_print.css" %>
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


<div class="page_frame">
    <table class="barcode_table">
        <c:forEach var="i" begin="1" end="5" step="1">
            <tr class="barcode_row">
                <td class="barcode_cell">
                    <img src="${pageContext.servletContext.contextPath}/generatebarcode?barcodeText=${eachTerminal.inventoryId}&displayText=${eachTerminal.regId}"
                         width="148" height="51">
                        ${eachTerminal.regId}
                </td>
            </tr>
        </c:forEach>
    </table>
</div>


</c:forEach>
</body>
</html>

