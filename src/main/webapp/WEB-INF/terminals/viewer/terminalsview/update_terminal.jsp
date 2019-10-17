<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file="/resources/css/terminals/terminal_update.css" %>
</style>
<script src="resources/js/jquery-3.4.1.min.js"></script>

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
            var model = form.model.value;
            var serialId = form.serialId.value;
            var inventoryId = form.inventoryId.value;
            var comment = form.comment.value;

            if (!validateLength(regId, 3, 10)) {
                result = false;
                infoBlock.innerHTML = '"Учетный номер" должен быть от 3 до 10 символов и не содержать пробелы, или оставьте пустым';
            }
            if (!validateLength(model, 3, 20)) {
                result = false;
                infoBlock.innerHTML = '"Модель" должна быть от 3 до 20 символов и не содержать пробелы, или оставьте пустым';
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

        function displayTerminalInfo(data) {
            var terminal = data;
            var form = document.getElementById("updateform");
            var regId = form.regId;
            regId.setAttribute("placeholder", terminal.regId);
            var model = form.model;
            model.setAttribute("placeholder", terminal.terminalModel);
            var serialId = form.serialId;
            serialId.setAttribute("placeholder", terminal.serialId);
            var inventoryId = form.inventoryId;
            inventoryId.setAttribute("placeholder", terminal.inventoryId);
            var comment = form.comment;
            comment.setAttribute("placeholder", terminal.terminalComment);
            var isActive = form.isActive;
            isActive.checked  = terminal.terminalIsActive;
            var selectorsOfDepartmentSelector = document.getElementsByTagName("option");
            for (var i = 0; i < selectorsOfDepartmentSelector.length; i++) {
                if(selectorsOfDepartmentSelector[i].value === terminal.departmentName) {
                    selectorsOfDepartmentSelector[i].selected = true;
                }
            }

        }

        function getAndDisplayDepartments() {
            sendAjaxRequest("getListOfDeparts", displayDepartmentsSelector);
        }

        function getAndDisplayTerminalInfo() {
            var jsonObj = JSON.stringify({"getTerminalInfo":${param.id}});
            sendAjaxRequest(jsonObj, displayTerminalInfo);
        }

        getAndDisplayDepartments();
        getAndDisplayTerminalInfo();






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
                <td><input class="input" type='text' name='regId' AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Модель:</td>
                <td><input class="input" type='text' name='model' AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Серийный номер:</td>
                <td><input class="input" type='text' name='serialId' AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Инв. номер:</td>
                <td><input class="input" type='text' name='inventoryId' AUTOCOMPLETE="off"/></td>
            </tr>
            <tr>
                <td class="cell">Комментарий:</td>
                <td><textarea name="comment" class="commentField" cols="32" rows="7"
                               AUTOCOMPLETE="off" style="resize: none"></textarea></td>
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

