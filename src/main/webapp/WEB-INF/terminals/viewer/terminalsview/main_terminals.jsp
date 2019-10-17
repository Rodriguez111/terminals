<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/terminals/terminals_main.css" %></style>
<style><%@include file="/resources/css/terminals/terminal_delete_popup.css" %></style>
<style><%@include file="/resources/css/terminals/terminal_error_popup.css" %></style>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Терминалы</title>

</head>
<body>

<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Version: 1.0.1<br>Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
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

<div id="info_container">
    <div class="check_box_container">
    <label for="show_only_active">Только активные</label>    <input id="show_only_active" type="checkbox">
    </div>
    <div id="countOfAllTerminals_container">
    </div>
    <div id="countOfActiveTerminals_container">
    </div>
</div>


<div class="main_block">
    <table class="main_table" id="main_table">
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
        elements = document.querySelectorAll('.regIdToDelete');
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
        console.log(buttonObj);
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


<script>

    window.onload = function () {

        showActiveTerminals();
        checkBox.checked = true;
    };


    var checkBox = document.getElementById("show_only_active");

    checkBox.addEventListener('change', function () {
        if(this.checked) {
            showActiveTerminals();
        } else {
            showAllTerminals();
        }
    });


    function showAllTerminals() {
        sendAjaxRequest("getAllTerminals", getTerminals);
    }

    function showActiveTerminals() {
        sendAjaxRequest("getActiveTerminals", getTerminals);
    }

    function getCountOfAllTerminals() {
        sendAjaxRequest("getCountOfAllTerminals", showCountOfAllUsers);
    }

    function getCountOfActiveTerminals() {
        sendAjaxRequest("getCountOfActiveTerminals", showCountOfActiveUsers);
    }

    function showCountOfAllUsers(data) {
        document.getElementById("countOfAllTerminals_container").innerHTML = "Всего: " + data.countOfAllTerminals;
    }

    function showCountOfActiveUsers(data) {
        document.getElementById("countOfActiveTerminals_container").innerHTML = "Активных: " + data.countOfActiveTerminals;
    }



    function getTerminals(data) {
        getCountOfAllTerminals();
        getCountOfActiveTerminals();

    var mainTable = document.getElementById("main_table");
    var table = "<tr class=\"table_header\">\n" +
        "<th id=\"regIdColumn\">Учетный номер</th>\n" +
        "<th>Модель</th>\n" +
        "<th>Серийный номер</th>\n" +
        "<th>Инв. номер</th>\n" +
        "<th>Комментарий</th>\n" +
        "<th>Департамент</th>\n" +
        "<th>Выдан пользователю</th>\n" +
        "<th>Активен</th>\n" +
        "<th>Дата создания</th>\n" +
        "<th>Дата изменения</th>\n";
        if(${role == 'root'}) {
            table += "<th id=\"updateColumn\">Редактировать</th>\n" +
            "<th id=\"deleteColumn\">Удалить</th>"
        }
        table += " </tr>";
    var listOfTerminals = data.listOfTerminals;
   for(var i = 0; i < listOfTerminals.length; i++) {
       var active = listOfTerminals[i].terminalIsActive ? "Да" : "Нет";

      table += "<tr class='row'><td class='cell'>" + listOfTerminals[i].regId + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].terminalModel + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].serialId + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].inventoryId + "</td>"
       +"<td class='cell' style='max-width: 200px'>" + listOfTerminals[i].terminalComment + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].departmentName + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].userLogin + "</td>"
       +"<td class='cell'>" + active + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].createDate + "</td>"
       +"<td class='cell'>" + listOfTerminals[i].lastUpdateDate + "</td>";

       if(${role == 'root'}) {
           table += "<td class='cell' style='text-align: center'>"
           + "<form class='forms' method='post' action='${pageContext.servletContext.contextPath}/selector'>"

           + "<input type='hidden' name='id' value=" + listOfTerminals[i].id + ">"
           + "<input type='hidden' name='action' value='update_terminal'/>"
           + "<input type='submit' value='Изменить'>"
           + "</form></td>"
           + "<td class='cell' style='text-align: center'>"
           + "<form class='delete_forms' method='post' action='${pageContext.servletContext.contextPath}/deleteterminal' >"
           + "<input type='hidden' name='id' value=" + listOfTerminals[i].id + ">"
           + "<input type='hidden' name='userLogin' value=" + listOfTerminals[i].userLogin + ">"
           + "<input type='hidden' name='regIdToDelete' class='regIdToDelete' value=" + listOfTerminals[i].regId + ">"
           + "<input type='hidden' name='action' value='delete_terminal'/>"
           + " </form>"
           + "<button class='deleteBtn' onclick='modalWin(this)'>Удалить</button>"
           + "</td>";
       }
      table += "</tr>";
   }
    table += "<tr>"
        + "<td id='table_footer' colspan='12'>"
        + "<div id='create_button'>"
        + "<form method='post' action='${pageContext.servletContext.contextPath}/selector'>"
        + "<input type='hidden' name='action' value='add_terminal'/>"
        + "<input type='submit' value='Создать'>"
        + "</form></div>"
        + "<div id='generate_button'><form>"
        + "<c:set scope='session' var='listOfTerminals' value='${listOfTerminals}' />"
        + "<input type='button' value='Генерировать ШК' onClick='location.href=\"${pageContext.servletContext.contextPath}/displaybarcodes\"'>"
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
