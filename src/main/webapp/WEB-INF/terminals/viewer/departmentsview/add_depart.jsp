<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/departs/depart_add.css"%></style>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Добавление департамента</title>
    <script>
        function validate() {
            var infoBlock = document.getElementById('sys_info');
            infoBlock.innerHTML = '';
            var form = document.getElementById("addform");
            var result = true;
            var department = form.department.value;

            if (department == '') {
                result = false;
                infoBlock.innerHTML = 'Название не может быть пустым';
            } else if (!validateLength(department, 3, 30)) {
                result = false;
                infoBlock.innerHTML = 'Название должно быть от 3 до 30 символов и не содержать пробелы';
            }
            if (result) {
                form.submit();
            }
            return result;
        }
        function validateLength(string, minLength, maxLength) {
            return string.length >= minLength && string.length <= maxLength && string.indexOf(' ') === - 1  && string.indexOf('\t') === -1;
        }

    </script>


</head>
<body>

<div class="depart_info">
    <div class="header">Создание нового департамента: <hr></div>
    <form class="form" method='post' action="${pageContext.servletContext.contextPath}/adddepart" id="addform" >
        <table class="table" >

            <tr><td class="cell">Название:  </td><td><input class="input" type='text' name='department' required   AUTOCOMPLETE="off"/></td></tr>

        </table>
        <hr>
        <div class="left_button"><input type='button' onclick="return validate();" value='Создать'></div>
    </form>

        <div class="right_button">  <form  action="${pageContext.servletContext.contextPath}/departs">
            <button>Назад</button>
        </form></div>

    <div class="sys_info" id="sys_info">
        <c:if test="${sysMessage != ''}">${sysMessage}</c:if>
    </div>



</div>




</body>
</html>


