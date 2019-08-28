<style><%@include file="/resources/css/main/main_menu.css" %></style>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Выход</title>
</head>
<body>


<div class='login_block'>
    <form method='post' action="${pageContext.servletContext.contextPath}<%="/login"%>">
        <table>
            <tr>
                <td><label>Login: </label></td>
                <td><label><input type='text' name='login'></label></td>
            </tr>
            <tr>
                <td><label>Password: </label></td>
                <td><label><input type='password' name='password'></label></td>
            </tr>
            <tr>
                <td colspan="2" style="font-size: 13px; color: red; text-align: center; padding: 0" height="16">
                    <c:if test="${errorMessage != ''}">
                        <c:out value="${errorMessage}"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td height="16"></td>
                <td><input type='submit' value='Войти'></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
