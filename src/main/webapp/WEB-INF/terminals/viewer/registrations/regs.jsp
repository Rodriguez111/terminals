<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>


<style>
    <%@include file="/resources/css/main/main_menu.css" %>
</style>
<style>
    <%@include file="/resources/css/registrations/regs.css" %>
</style>
<style>
    <%@include file="/resources/css/registrations/regs_error_popup.css" %>
</style>
<script>
    <%@include file="/WEB-INF/terminals/viewer/registrations/regs.js" %>
</script>
<script src="resources/js/jquery-3.4.1.min.js"></script>

<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Учет приема-выдачи</title>
</head>
<body onload="onWindowLoad()">


<div class="main_block">

    <table class="main_table">
        <tbody>
        <tr class="top_half">
            <td class="left_up_quarter">
                <div class="topWindowName"> Информация о терминалах</div>

                <div class="terminalInputBlock">
                    <div class="terminal"> Терминал</div>

                    <input id="terminalInput" class="terminalInput" type="password" name="terminalInvId" size="20"
                           autocomplete="off">

                </div>

                <div class="terminalInfoBlock">
                    <div class="terminalNo"> № терминала</div>
                    <div id="terminalNoInfo" class="terminalNoInfo"></div>
                </div>

                <div class="operationTypeBlock">
                    <div class="operationType"> Тип операции</div>
                    <div id="operationTypeInfo" class="operationTypeInfo"></div>

                </div>

                <div class="terminalsTotalBlock">
                    <div class="terminalsTotal">Всего</div>
                    <div id="terminalsTotalInfo" class="terminalsTotalInfo">${totalAmountOfTerminals}</div>
                </div>

                <div class="terminalsInactiveBlock">
                    <div class="terminalsInactive">Неактивных</div>
                    <div id="terminalsInactiveInfo" class="terminalsInactiveInfo">${amountOfInactiveTerminals}</div>
                </div>

                <div class="terminalsGivenBlock">
                    <div class="terminalsGiven">Выдано</div>
                    <div id="terminalsGivenInfo" class="terminalsGivenInfo">${amountOfGivenTerminals}</div>
                </div>

                <div class="terminalsRemainBlock">
                    <div class="terminalsRemain">Осталось активных</div>
                    <div id="terminalsRemainInfo" class="terminalsRemainInfo">${activeTerminalsRemain}</div>
                </div>



            </td>

            <td class="right_up_quarter">
                <div id="terminalImageBlock" class="terminalImageBlock">

                </div>

            </td>
        </tr>
        <tr class="bottom_half">
            <td class="left_bottom_quarter">
                <div class="bottomWindowName"> Информация о пользователях</div>
                <div class="userInputBlock">
                    <div class="user"> Пользователь</div>
                    <input id="userInput" class="userInput" type="password" name="userLogin" size="20"
                           AUTOCOMPLETE="off" >

                </div>

                <div class="userNameBlock">
                    <div class="userName"> Фамилия и имя пользователя</div>
                    <div id="userNameInfo" class="userNameInfo"></div>
                </div>

                <div class="adminNameBlock">
                    <div class="adminName"> Фамилия и имя администратора</div>
                    <div id="adminNameInfo" class="adminNameInfo">${surname} ${name}</div>
                </div>


            </td>
            <td class="right_bottom_quarter">
                <div id="userImageBlock" class="userImageBlock">
                </div>
            </td>
        </tr>
        </tbody>
    </table>

</div>

<input type="hidden" id="adminLogin" value="${sessionScope.login}" />

<div class="modal-overlay">
    <div class="modal-wrapper">
        <div class="modal-header">
            Ошибка!
        </div>
        <div class="modal-body" id="modal-body">
            <div class="info-field" id="info-field">

            </div>
        </div>
        <div class="modal-footer">
            <button id="ok-error" class="ok-error">Ok</button>
        </div>
    </div>
</div>



</body>
</html>
