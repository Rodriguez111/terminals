<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file="/resources/css/users/user_add.css" %>
</style>
<script src="resources/js/jquery-3.4.1.min.js"></script>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Создание пользователя</title>
    <script>
        function validate() {
            var infoBlock = document.getElementById('sys_info');
            infoBlock.innerHTML = '';
            var form = document.getElementById("addform");
            var result = true;
            var login = form.login.value;
            var password = form.password.value;
            var rePassword = form.rePassword.value;
            var name = form.name.value;
            var surname = form.surname.value;
            if (login == '') {
                result = false;
                infoBlock.innerHTML = 'Поле Логин не может быть пустым';
            } else if(!validateLength(login, 20)) {
                result = false;
                infoBlock.innerHTML = 'Логин должен быть от 3 до 20 символов и не содержать пробелы';
            }
            else if (password == '') {
                result = false;
                infoBlock.innerHTML = 'Поле Пароль не может быть пустым';
            } else if(!validateLength(password, 20)) {
                result = false;
                infoBlock.innerHTML = 'Пароль должен быть от 3 до 20 символов и не содержать пробелы';
            }
            else if (password != rePassword) {
                result = false;
                infoBlock.innerHTML = 'Пароли не совпадают';
            } else if (name == '') {
                result = false;
                infoBlock.innerHTML = 'Поле Имя не может быть пустым';
            } else if(!validateLength(name, 25)) {
                result = false;
                infoBlock.innerHTML = 'Имя должно быть от 3 до 25 символов и не содержать пробелы';
            } else if (surname == '') {
                result = false;
                infoBlock.innerHTML = 'Поле Фамилия не может быть пустым';
            } else if(!validateLength(surname, 25)) {
                result = false;
                infoBlock.innerHTML = 'Фамилия должна быть от 3 до 25 символов и не содержать пробелы';
            }
            if(result) {
              form.submit() ;
            }
            return result;
        }

        function validateLength(string, maxLength) {
            return string.length >= 3 && string.length <= maxLength && string.indexOf(' ') === - 1  && string.indexOf('\t') === -1;
        }


        function sendAjaxRequest(dataToSend, callback) {
            $.ajax('./json', {
                method:'post',
                data:dataToSend,
                contentType:'text/json; charset=utf-8',
                dataType:'json',
                success:function (data) {
                    callback(data);
                }
            })
        }

        function displayDepartmentsSelector(data) {
            var listOfDepartments = data.listOfDeparts;
            var selector = document.getElementById("departmentsSelector");
            var options = document.createElement("option");
            options.selected = true;
            options.setAttribute("value", "");
            options.innerHTML = "Не выбран";
            selector.appendChild(options);
            for (var i = 0; i < listOfDepartments.length; i++) {
                options = document.createElement("option");
                options.setAttribute("value", listOfDepartments[i]);
                options.innerHTML = listOfDepartments[i];
                selector.appendChild(options);
            }
        }

        function getAndDisplayDepartments() {
            sendAjaxRequest("getListOfDeparts", displayDepartmentsSelector);
        }

        function displayRolesSelector(data) {
            var listOfRoles = data.listOfRoles;
            var selector = document.getElementById("rolesSelector");
            for (var i = 0; i < listOfRoles.length; i++) {
                options = document.createElement("option");
                options.setAttribute("value", listOfRoles[i]);
                options.innerHTML = listOfRoles[i];
                if (listOfRoles[i] === "user") {
                    options.selected = true;
                }
                selector.appendChild(options);
            }
        }

        function getAndDisplayRoles() {
            sendAjaxRequest("getListOfRoles", displayRolesSelector);
        }
        getAndDisplayDepartments();
        getAndDisplayRoles();

    </script>
</head>
<body>

<div class="user_info">
    <div class="header">Создание нового пользователя:
        <hr>
    </div>
    <form class="form" method='post' action="${pageContext.servletContext.contextPath}/adduser" id="addform">
        <table class="table">


            <tr>
                <td class="cell">Логин:</td>
                <td><input class="input" type='text' name='login' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Пароль:</td>
                <td><input class="input" type='password' name='password' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Подтверждение пароля:</td>
                <td><input class="input" type='password' name='rePassword' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Имя:</td>
                <td><input class="input" type='text' name='name' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Фамилия:</td>
                <td><input class="input" type='text' name='surname' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Роль:</td>
                <td><select id="rolesSelector" class="input" required name="role" form="addform">
<%--                    <option selected >user</option>--%>
<%--                    <c:forEach items="${param.listOfRoles}" var="eachRole">--%>
<%--                        <option value="${eachRole.replaceAll('^\\[|\\]$|\\s', '')}">${eachRole.replaceAll('^\\[|\\]$|\\s', '')}</option>--%>
<%--                    </c:forEach>--%>
                </select></td>
            </tr>

            <tr>
                <td class="cell">Департамент:</td>
                <td><select id="departmentsSelector" class="input" name="department" form="addform">
<%--                    <option selected ></option>--%>
<%--                    <c:forEach items="${param.listOfDeparts}" var="eachDepart">--%>
<%--                        <option value="${eachDepart.replaceAll('^\\[|\\]$|\\s', '')}">${eachDepart.replaceAll('^\\[|\\]$|\\s', '')}</option>--%>
<%--                    </c:forEach>--%>
                </select></td>
            </tr>


            <tr>
                <td class="cell">Активен:</td>
                <td>
                    <input type='checkbox' name='isActive' value="true" checked/>
                </td>
            </tr>
        </table>


        <hr>
        <div class="left_button"><input type='button' onclick="return validate();" value='Создать'></div>
    </form>

    <div class="right_button">
        <form action="${pageContext.servletContext.contextPath}/users">
            <button>Назад</button>
        </form>
    </div>

    <div class="sys_info" id="sys_info">
        <c:if test="${sysMessage != ''}">${sysMessage}</c:if>
    </div>


</div>


</body>
</html>


