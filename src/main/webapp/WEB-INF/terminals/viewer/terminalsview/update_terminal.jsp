<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file="/resources/css/terminals/terminal_update.css" %>
</style>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Редактирование терминала</title>

    <script>
        function validate() {
            var result = true;
            var infoBlock = document.getElementById('sys_info');
            infoBlock.innerHTML = '';
            var form = document.getElementById("updateform");
            var regId = form.regId.value;
            var serialId = form.serialId.value;
            var inventoryId = form.inventoryId.value;
            var comment = form.comment.value;

            if (!validateLength(regId, 3, 10)) {
                result = false;
                infoBlock.innerHTML = '"Учетный номер" должен быть от 3 до 10 символов и не содержать пробелы, или оставьте пустым';
            }
            if (!validateLength(serialId, 3, 30)) {
                result = false;
                infoBlock.innerHTML = '"Серийный номер" должен быть от 3 до 30 символов и не содержать пробелы, или оставьте пустым';
            }
            if (!validateLength(inventoryId, 3, 20)) {
                result = false;
                infoBlock.innerHTML = '"Инв. номер" должен быть от 3 до 20 символов и не содержать пробелы, или оставьте пустым';
            }
            if (!validateComment(comment, 500)) {
                result = false;
                infoBlock.innerHTML = 'Поле "Комментарий" не должно содержать более 500 символов ' +
                    'и не должно содержать двойных пробелов, или оставьте пустым';
            }
            if (result) {
                form.submit();
            }
        }

        function validateLength(string, minLength, maxLength) {
            return string.length == 0 || (string.length >= minLength && string.length <= maxLength && !string.includes(' ') && !string.includes('\t'));
        }

        function validateComment(string, maxLength) {
            return string.length == 0 || (string.length <= maxLength && !string.includes('  '));
        }

    </script>

</head>
<body>

<div class="terminal_info">
    <div class="header">Редактирование профиля терминала:
        <hr>
    </div>
    <form class="form" method='post' action="${pageContext.servletContext.contextPath}/updateterminal" id="updateform">
        <input type='hidden' name='id' value='${param.id}'/>
        <table class="table">
            <tr>
                <td class="cell">Учетный номер:</td>
                <td><input class="input" type='text' name='regId' placeholder="${param.regId}"  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Серийный номер:</td>
                <td><input class="input" type='text' name='serialId' placeholder="${param.serialId}"  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Инв. номер:</td>
                <td><input class="input" type='text' name='inventoryId' placeholder="${param.inventoryId}"  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Комментарий:</td>
                <td><textarea name="comment" class="commentField" cols="32" rows="7"
                              placeholder="${param.comment}"  AUTOCOMPLETE="off"></textarea></td>
            </tr>
            <tr>
                <td class="cell">Департамент:</td>
                <td><select class="input" name="department" form="updateform">
                    <option selected>${param.department}</option>
                    <c:forEach items="${param.listOfDeparts}" var="eachDepart">
                        <option value="${eachDepart.replaceAll('^\\[|\\]$|\\s', '')}">${eachDepart.replaceAll('^\\[|\\]$|\\s', '')}</option>
                    </c:forEach>
                </select></td>
            </tr>
            <tr>
                <td class="cell">Активен:</td>
                <td><input type='checkbox' name='isActive'
                           <c:if test="${param.isActive == true}">checked</c:if> /></td>
            </tr>
        </table>

        <hr>
        <div class="left_button"><input type='button' onclick="validate();" value='Изменить'></div>
    </form>

    <div class="right_button">
        <form action="${pageContext.servletContext.contextPath}/terminals">
            <button>Назад</button>
        </form>
    </div>

    <div class="sys_info" id="sys_info">
        <c:if test="${sysMessage != ''}">${sysMessage}</c:if>
    </div>

</div>

</body>
</html>

