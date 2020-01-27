<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file="/resources/css/users/user_update.css" %>
</style>
<script src="resources/js/jquery-3.4.1.min.js"></script>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Редактирование пользователя</title>

    <script>
        window.onload = function () {
            loadPage();

        };


        function validate() {
            var result = true;
            var infoBlock = document.getElementById('sys_info');
            infoBlock.innerHTML = '';
            var form = document.getElementById("updateform");
            var login = form.login.value;
            var name = form.name.value;
            var surname = form.surname.value;
            var password = form.password.value;
            var rePassword = form.rePassword.value;

            if (!validateLength(login, 20)) {
                result = false;
                infoBlock.innerHTML = 'Логин должен быть от 3 до 20 символов и не содержать пробелы, или оставьте пустым';
            }
            if (password.length != 0) {
                if (!validateLength(password, 20)) {
                    result = false;
                    infoBlock.innerHTML = 'Пароль должен быть от 3 до 20 символов и не содержать пробелы, или оставьте пустым';
                } else if (password != rePassword) {
                    result = false;
                    infoBlock.innerHTML = 'Пароли не совпадают';
                }
            }
            if (!validateLength(name, 25)) {
                result = false;
                infoBlock.innerHTML = 'Имя должно быть от 3 до 25 символов и не содержать пробелы, или оставьте пустым';
            }
            if (!validateLength(surname, 25)) {
                result = false;
                infoBlock.innerHTML = 'Фамилия должна быть от 3 до 25 символов и не содержать пробелы, или оставьте пустым';
            }
            if (result) {
                form.submit();
            }
        }

        function validateLength(string, maxLength) {
            return string.length == 0 || (string.length >= 3 && string.length <= maxLength && string.indexOf(' ') === - 1  && string.indexOf('\t') === -1);
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
            getAndDisplayUserInfo();

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
                selector.appendChild(options);
            }
             getAndDisplayDepartments()
        }

        function getAndDisplayRoles() {
            sendAjaxRequest("getListOfRoles", displayRolesSelector);
        }

        function displayUserInfo(data) {
            var user = data;
            var form = document.getElementById("updateform");
            var login = form.login;
            login.setAttribute("placeholder", user.userLogin);
            if(user.userLogin === "root") {
               login.setAttribute("disabled", true);
            }
            var name = form.name;
            name.setAttribute("placeholder", user.userName);
            var surname = form.surname;
            surname.setAttribute("placeholder", user.userSurname);
            var isActive = form.isActive;
            isActive.checked  = user.active;
            if (user.userLogin === 'root') {
                isActive.disabled  = true;
            }

            var selectorsOfDepartmentSelector = document.getElementById("departmentsSelector")
                .getElementsByTagName("option");
            for (var i = 0; i < selectorsOfDepartmentSelector.length; i++) {
                if(selectorsOfDepartmentSelector[i].value === user.userDepartment) {

                    selectorsOfDepartmentSelector[i].selected = true;
                }
            }

            var selectorsOfRoleSelector = document.getElementById("rolesSelector")
                .getElementsByTagName("option");

            if (user.userLogin === 'root') {
                var roleSelector = document.getElementById("rolesSelector");
                roleSelector.disabled = true;
                var option = document.createElement("option");
                option.setAttribute("value", "root");
                option.setAttribute("selected", true);
                option.innerHTML = "root";
                roleSelector.appendChild(option);
            } else {

                for (var i = 0; i < selectorsOfRoleSelector.length; i++) {
                    if(selectorsOfRoleSelector[i].value === user.userRole) {
                        selectorsOfRoleSelector[i].selected = true;
                    }
                }
            }
        }

        function loadPage() {
            getAndDisplayRoles();
        }

        function getAndDisplayUserInfo() {
            var jsonObj = JSON.stringify({"getUserInfo":${param.id}});
            sendAjaxRequest(jsonObj, displayUserInfo);
        }





    </script>

</head>
<body>

<div class="user_info">
    <div class="header">Редактирование профиля пользователя:
        <hr>
    </div>
    <form class="form" method='post' action="${pageContext.servletContext.contextPath}/updateuser" id="updateform">
        <input type='hidden' name='id' value='${param.id}'/>
        <table class="table">

            <tr>
                <td class="cell">Логин:</td>
                <td><input class="input" type='text' name='login' placeholder="${param.login}" <c:if test="${param.role == 'root'}">disabled</c:if>
                           AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Пароль:</td>
                <td><input class="input" type='password' name='password'  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Подтверждение пароля:</td>
                <td><input class="input" type='password' name='rePassword'  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Имя:</td>
                <td><input class="input" type='text' name='name' placeholder="${param.name}"  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Фамилия:</td>
                <td><input class="input" type='text' name='surname' placeholder="${param.surname}"  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Роль:</td>
                <td><select id="rolesSelector" class="input" name="role" form="updateform" <c:if test="${param.role == 'root'}">disabled</c:if>>
                </select></td>
            </tr>

            <tr>
                <td class="cell">Департамент:</td>
                <td><select id="departmentsSelector" class="input" name="department" form="updateform">
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

