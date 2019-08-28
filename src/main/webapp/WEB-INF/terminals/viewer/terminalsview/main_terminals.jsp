<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/terminals/terminals_main.css" %></style>
<style><%@include file="/resources/css/terminals/terminal_delete_popup.css" %></style>
<style><%@include file="/resources/css/terminals/terminal_error_popup.css" %></style>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Терминалы</title>

</head>
<body>

<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
        <ul id="main_menu">
            <li class="li" id="left_button"><a class="common_button" href="${pageContext.servletContext.contextPath}/main">Главная</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/users">Пользователи</a></li>
            <li class="li"><a class="active_button" href="${pageContext.servletContext.contextPath}/terminals">Терминалы</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/departs">Департаменты</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/regs">Учет</a></li>
            <li id="right_button"> <a class="adminInformation" href=""><c:out value="${name} ${surname}"/>  <br> роль: <c:out value="${role}"/> </a>
                <ul id="sub_menu">
                    <li id="sub_li"><a class="exit" href="${pageContext.servletContext.contextPath}/logout">Выход</a></li>
                </ul>
            </li>
        </ul>
    </div>
</div>

<div class="first_separator">
    <hr>
</div>


<div class="main_block">
    <table class="main_table" id="main_table">
        <tr class="table_header">
            <th>Учетный номер</th>
            <th>Серийный номер</th>
            <th>Инв. номер</th>
            <th>Комментарий</th>
            <th>Департамент</th>
            <th>Выдан пользователю</th>
            <th>Активен</th>
            <th>Дата создания</th>
            <th>Дата изменения</th>
    <c:if test="${role == 'root'}">
            <th>Обновить профиль</th>
            <th>Удалить профиль</th>
    </c:if>
        </tr>

        <c:forEach items="${listOfTerminals}" var="eachTerminal">
            <tr class='row'>
                <td class='cell'>${eachTerminal.regId}</td>
                <td class='cell'>${eachTerminal.serialId}</td>
                <td class='cell'>${eachTerminal.inventoryId}</td>
                <td class='cell'>${eachTerminal.terminalComment}</td>
                <td class='cell'>${eachTerminal.departmentName}</td>
                <td class='cell'>${eachTerminal.userLogin}</td>
                <td class='cell'>${eachTerminal.terminalIsActive == true? 'Да' : 'Нет'}</td>
                <td class='cell'>${eachTerminal.createDate}</td>
                <td class='cell'>${eachTerminal.lastUpdateDate}</td>


                <c:if test="${role == 'root'}">
                <td class='cell'>


                        <form class="forms" method='post'
                              action="${pageContext.servletContext.contextPath}/selector">
                            <input type='hidden' name='id' value='${eachTerminal.id}'/>
                            <input type='hidden' name='regId' value='${eachTerminal.regId}'/>
                            <input type='hidden' name='serialId' value='${eachTerminal.serialId}'/>
                            <input type='hidden' name='inventoryId' value='${eachTerminal.inventoryId}'/>
                            <input type='hidden' name='comment' value='${eachTerminal.terminalComment}'/>
                            <input type='hidden' name='department' value='${eachTerminal.departmentName}'/>
                            <input type='hidden' name='isActive' value='${eachTerminal.terminalIsActive}'/>
                            <input type='hidden' name='listOfDeparts' value='${listOfDeparts}'/>
                            <input type='hidden' name='action' value='update_terminal'/>
                            <input type='submit' value='Изменить'>
                        </form>

                </td>


                <td class='cell'>

                    <c:if test="${eachUser.userRole != 'root'}">
                        <form class="delete_forms" method='post' action="${pageContext.servletContext.contextPath}/deleteterminal" >
                            <input type='hidden' name='id' value='${eachTerminal.id}'/>
                            <input type='hidden' name='userLogin' value='${eachTerminal.userLogin}'/>
                            <input type='hidden' name='regIdToDelete' class="regIdToDelete" value='${eachTerminal.regId}'/>
                            <input type='hidden' name='action' value='delete_terminal'/>
                        </form>
                        <button class="deleteBtn" onclick="modalWin(this)">Удалить</button>
                    </c:if>
                </td>
                </c:if>
            </tr>

        </c:forEach>

        <tr>
            <td id="table_footer" colspan="10">
                <div id="create_button">
                    <form method='post' action="${pageContext.servletContext.contextPath}/selector">
                        <input type='hidden' name='action' value='add_terminal'/>
                        <input type='hidden' name='listOfDeparts' value='${listOfDeparts}'/>
                        <input type='hidden' name='listOfRoles' value='${listOfRoles}'/>
                        <input type='submit' value='Создать'>
                    </form>
                </div>
                <div id="generate_button">
                    <form>
                        <c:set scope="session" var="listOfTerminals" value="${listOfTerminals}" />
                        <input type='hidden' name='listOfTerminals' value='${listOfTerminals}'/>
                        <input type="button" value="Генерировать ШК" onClick='location.href="${pageContext.servletContext.contextPath}/displaybarcodes"'>
                    </form>
                </div>


            </td>
        </tr>

    </table>
</div>


<div class="modal">
    <div class="modal-wrapper">
        <div class="modal-header">
            <span class="closeButton">&times;</span>
            Удаление
        </div>
        <div class="modal-body" id="modal-body">

        </div>
        <div class="modal-footer">
            <button id="ok" class="ok">Да</button>
            <button id="cancel" class="cancel">Отменить</button>
        </div>
    </div>
</div>

<div class="modal-error">
    <div class="modal-wrapper-error">
        <div class="modal-header-error">
            <span class="closeButton-error">&times;</span>
            Ошибка
        </div>
        <div class="modal-body" id="modal-body-error">

        </div>
        <div class="modal-footer-error">
            <button id="ok-error" class="ok-error">Ok</button>
        </div>
    </div>
</div>



<script>
    var button = document.querySelectorAll('.deleteBtn'); //получаем массив классов deleteBtn (кнопки Удалить)
    var form = document.querySelectorAll('.delete_forms'); //получаем массив классов delete_forms (кнопки форма удаления)
    for (var i = 0; i < button.length; i++) { //в цикле создаем прослушиватель события для каждой кнопки
        addListener(button[i])
    }

    function addListener(button) { //функция, вешающая прослушиватель на переданный в нее элемент
        button.addEventListener('click', function () {
            modal.style.display = 'flex';
        });
    }

    var modal = document.querySelector('.modal');
    var closeBtn = document.querySelector('.closeButton');

    var okModalBtn = document.querySelector('.ok');
    var cancelModalBtn = document.querySelector('.cancel');
    var elements = document.querySelectorAll('.regIdToDelete');

    closeBtn.addEventListener('click', function () { //При нажатии Крестик убираем модальное окно.
        modal.style.display = 'none';
    });

    function modalWin(buttonObj) {
        var count = 0;
        for (var i = 0; i < button.length; i++) { //Определяем, какая кнопка нажата
            if(button[i] == buttonObj) {
                count = i;
            }
        }
        var terminal = elements[count].getAttribute("value");
        var infoBlock = document.getElementById('modal-body');
        infoBlock.innerHTML = 'Вы действительно хотите удалить терминал <b>' + terminal + '</b>?';
        okModalBtn.addEventListener('click', function () { //При нажатии Да убираем модальное окно и отправляем форму.
            modal.style.display = 'none';
            form[count].submit(); //сабмитим именно ту форму, которая соответствует нажатой кнопке.
        });
        cancelModalBtn.addEventListener('click', function () { //При нажатии Отмена убираем модальное окно.
            modal.style.display = 'none';
        });
    }

</script>
<c:if test="${sessionScope.sysMessage != ''}">
    <div id="sysMessage" sysMessage="${sessionScope.sysMessage}" />
    <script>
        var sysMessage = document.getElementById("sysMessage");
        var message = sysMessage.getAttribute("sysMessage");
        var modalError = document.querySelector('.modal-error');
        var infoBlockError = document.getElementById('modal-body-error');
        var okModalErrBtn = document.querySelector('.ok-error');
        var closeErrBtn = document.querySelector('.closeButton-error');

        closeErrBtn.addEventListener('click', function () { //При нажатии Крестик убираем модальное окно.
            modalError.style.display = 'none';
        });
        okModalErrBtn.addEventListener('click', function () { //При нажатии Крестик убираем модальное окно.
            modalError.style.display = 'none';
        });
        function modalWinSysMessage(message) {
            infoBlockError.innerHTML = message;
            modalError.style.display = 'flex';
        }
        if(message != '') {
            modalWinSysMessage(message);
        }
    </script>
    <% session.removeAttribute("sysMessage"); %>
</c:if>

</body>
</html>
