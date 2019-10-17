<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/users/users_main.css" %></style>
<style><%@include file="/resources/css/users/users_delete_popup.css" %></style>
<style><%@include file="/resources/css/users/users_error_popup.css" %></style>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Пользователи</title>
</head>
<body>
<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Version: 1.0.1<br>Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
        <ul id="main_menu">
            <li class="li" id="left_button"><a class="common_button" href="${pageContext.servletContext.contextPath}/main">Главная</a></li>
            <li class="li"><a class="active_button" href="${pageContext.servletContext.contextPath}/users">Пользователи</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/terminals">Терминалы</a></li>
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

<input type="hidden" id="roleOfLoggedInUser" name="role" value="${role}"/>

<div id="info_container">
    <div class="check_box_container">
        <label for="show_only_active">Только активные</label>    <input id="show_only_active" type="checkbox">
    </div>
    <div id="countOfAllUsers_container">
    </div>
    <div id="countOfActiveUsers_container">
    </div>
</div>

<div class="main_block">
    <table id="main_table" class="main_table">
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

    function prepareForModal() {
        elements = document.querySelectorAll('.userToDelete');
        button = document.querySelectorAll('.deleteBtn'); //получаем массив классов deleteBtn (кнопки Удалить)
        form = document.querySelectorAll('.delete_forms'); //получаем массив классов delete_forms (кнопки форма удаления)
        for (var i = 0; i < button.length; i++) { //в цикле создаем прослушиватель события для каждой кнопки
            addListener(button[i])
        }
    }
    var elements;
    var button;
    var form;


    function addListener(button) { //функция, вешающая прослушиватель на переданный в нее элемент
        button.addEventListener('click', function () {
            modal.style.display = 'flex';
        });
    }

    var modal = document.querySelector('.modal');
    var closeBtn = document.querySelector('.closeButton');

    var okModalBtn = document.querySelector('.ok');
    var cancelModalBtn = document.querySelector('.cancel');


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
        var user = elements[count].getAttribute("value");
        var infoBlock = document.getElementById('modal-body');
        infoBlock.innerHTML = 'Вы действительно хотите удалить пользователя <b>' + user + '</b>?';
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


<script>
    window.onload = function () {

        showActiveUsers();
        checkBox.checked = true;
    };

    var roleOfLoggedInUser = document.getElementById("roleOfLoggedInUser").value;

    var checkBox = document.getElementById("show_only_active");

    checkBox.addEventListener('change', function () {
        if(this.checked) {
            showActiveUsers();
        } else {
            showAllUsers();
        }
    });


    function showAllUsers() {
        sendAjaxRequest("getAllUsers", getUsers);
    }

    function showActiveUsers() {
        sendAjaxRequest("getActiveUsers", getUsers);
    }

    function getCountOfAllUsers() {
        sendAjaxRequest("getCountOfAllUsers", showCountOfAllUsers);
    }

    function getCountOfActiveUsers() {
        sendAjaxRequest("getCountOfActiveUsers", showCountOfActiveUsers);
    }

    function showCountOfAllUsers(data) {
        document.getElementById("countOfAllUsers_container").innerHTML = "Всего: " + data.countOfAllUsers;
    }

    function showCountOfActiveUsers(data) {
        document.getElementById("countOfActiveUsers_container").innerHTML = "Активных: " + data.countOfActiveUsers;
    }



    function getUsers(data) {
        getCountOfAllUsers();
        getCountOfActiveUsers();

        var mainTable = document.getElementById("main_table");
        var table = "<tr class=\"table_header\">\n" +
            "<th id=\"regIdColumn\">Логин</th>\n" +
            "<th>Фамилия</th>\n" +
            "<th>Имя</th>\n" +
            "<th>Роль</th>\n" +
            "<th>Департамент</th>\n" +
            "<th>Выдан терминал</th>\n" +
            "<th>Активен</th>\n" +
            "<th>Дата создания</th>\n" +
            "<th>Дата изменения</th>\n" +
         "<th id=\"updateColumn\">Редактировать</th>\n" +
                "<th id=\"deleteColumn\">Удалить</th>";

        table += " </tr>";
        var listOfUsers = data.listOfUsers;
        for(var i = 0; i < listOfUsers.length; i++) {
            var active = listOfUsers[i].active ? "Да" : "Нет";

            table += "<tr class='row'><td class='cell'>" + listOfUsers[i].userLogin + "</td>"
                +"<td class='cell'>" + listOfUsers[i].userSurname + "</td>"
                +"<td class='cell'>" + listOfUsers[i].userName + "</td>"
                +"<td class='cell'>" + listOfUsers[i].userRole + "</td>"
                +"<td class='cell'>" + listOfUsers[i].userDepartment + "</td>"
                +"<td class='cell'>" + listOfUsers[i].terminalRegId + "</td>"
                +"<td class='cell'>" + active + "</td>"
                +"<td class='cell'>" + listOfUsers[i].createDate + "</td>"
                +"<td class='cell'>" + listOfUsers[i].lastUpdateDate + "</td>";

            table += "<td class='cell' style=\"text-align: center\">";
                if(listOfUsers[i].userRole !== 'root' || roleOfLoggedInUser === 'root') {

                    table += "<form class='forms' method='post' action='${pageContext.servletContext.contextPath}/selector'>"
                        + "<input type='hidden' name='id' value='" + listOfUsers[i].id + "'/>"
                        + "<input type='hidden' name='action' value='update_user'/>"
                        + "<input type='submit' value='Изменить'>"
                        + "</form>";
                }

            table += "</td>";

            table += "<td class='cell' style='text-align: center'>";
            if(listOfUsers[i].userRole !== 'root' ) {
            table += "<form class='delete_forms' method='post' action='${pageContext.servletContext.contextPath}/deleteuser' >"
            + "<input type='hidden' name='id' value='" + listOfUsers[i].id + "'/>"
            + "<input type='hidden' name='loginToDelete' class='userToDelete' value='" + listOfUsers[i].userLogin + "'/>"
            + "<input type='hidden' name='action' value='delete_user'/>"
            + "</form>"
            + "<button class='deleteBtn' onclick='modalWin(this)'>Удалить</button>";

            }
            table += "</td>";

            table += "</tr>";
        }
        table += "<tr>"
            + "<td id='table_footer'  colspan='12'>"
            + "<div id='create_button'>"
            + "<form method='post' action='${pageContext.servletContext.contextPath}/selector'>"
            + "<input type='hidden' name='action' value='add_user'/>"
            + "<input type='submit' value='Создать'>"
            + "</form></div>"
            + "</td></tr>";
        mainTable.innerHTML = table;
        prepareForModal();
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

</script>


</body>
</html>
