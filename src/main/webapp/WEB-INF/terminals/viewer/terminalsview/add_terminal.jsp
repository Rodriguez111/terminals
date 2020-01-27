<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file="/resources/css/terminals/terminal_add.css" %>
</style>
<script src="resources/js/jquery-3.4.1.min.js"></script>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Добавление терминала</title>
    <script>
        function validate() {
            var infoBlock = document.getElementById('sys_info');
            infoBlock.innerHTML = '';
            var form = document.getElementById("addform");
            var result = true;
            var regId = form.regId.value;
            var model = form.model.value;
            var serialId = form.serialId.value;
            var inventoryId = form.inventoryId.value;
            var comment = form.comment.value;
            if (regId == '') {
                result = false;
                infoBlock.innerHTML = 'Поле "Учетный номер" не может быть пустым';
            } else if (!validateLength(regId, 3, 10)) {
                result = false;
                infoBlock.innerHTML = '"Учетный номер" должен быть от 3 до 10 символов и не содержать пробелы';
            } else if (!validateRegId(regId)) {
                result = false;
                infoBlock.innerHTML = 'Поле "Учетный номер" должно иметь числовое значение всех символов, кроме первых двух. Например AB12345';
            } else if (model == '') {
                result = false;
                infoBlock.innerHTML = 'Поле "Модель" не может быть пустым';
            } else if (!validateLength(model, 3, 20)) {
                result = false;
                infoBlock.innerHTML = 'Поле "Модель" должно быть от 3 до 20 символов и не содержать пробелы';
            } else if (serialId == '') {
                result = false;
                infoBlock.innerHTML = 'Поле "Серийный номер" не может быть пустым';
            } else if (!validateLength(serialId, 3, 30)) {
                result = false;
                infoBlock.innerHTML = 'Поле "Серийный номер" должно быть от 3 до 30 символов и не содержать пробелы';
            }  else if (inventoryId == '') {
                result = false;
                infoBlock.innerHTML = 'Поле "Инв. номер" не может быть пустым';
            } else if (!validateLength(inventoryId, 3, 20)) {
                result = false;
                infoBlock.innerHTML = 'Поле "Инв. номер" должно быть от 3 до 20 символов и не содержать пробелы';
            } else if (comment != '') {
                if (!validateComment(comment, 500)) {
                    result = false;
                    infoBlock.innerHTML = 'Поле "Комментарий" не должно содержать более 500 символов ' +
                        'и не должно содержать двойных пробелов';
                }
            }
            if (result) {
                form.submit();
            }
            return result;
        }
        function validateLength(string, minLength, maxLength) {
            return string.length >= minLength && string.length <= maxLength && string.indexOf(' ') === - 1  && string.indexOf('\t') === -1;
        }

        function validateComment(string, maxLength) {
            return string.length <= maxLength && string.indexOf('  ') === - 1  && string.indexOf('\t') === -1;
        }

        function validateRegId(string) {
            var regex = new RegExp("^[A-zА-я]{2}[0-9]{1,8}$");
            return regex.test(string);
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
        getAndDisplayDepartments();


    </script>
</head>
<body>

<div class="terminal_info">
    <div class="header">Создание нового пользователя:
        <hr>
    </div>
    <form class="form" method='post' action="${pageContext.servletContext.contextPath}/addterminal" id="addform">
        <table class="table">


            <tr>
                <td class="cell">Учетный номер:</td>
                <td><input class="input" type='text' name='regId' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Модель:</td>
                <td><input class="input" type='text' name='model' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Серийный номер:</td>
                <td><input class="input" type='text' name='serialId' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Инв. номер:</td>
                <td><input class="input" type='text' name='inventoryId' required  AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Комментарий:</td>
                <td><textarea name="comment" class="commentField" cols="32" rows="7"
                              AUTOCOMPLETE="off" style="resize: none"></textarea></td>
            </tr>
            <tr>
                <td class="cell">Департамент:</td>
                <td><select id="departmentsSelector" class="input" name="department" form="addform">
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


