<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/users/users_main.css" %></style>
<style><%@include file="/resources/css/users/users_delete_popup.css" %></style>
<style><%@include file="/resources/css/users/users_error_popup.css" %></style>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Пользователи</title>
</head>
<body>
<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
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

<div class="main_block">
    <table class="main_table">
        <tr class="table_header">
            <th>Логин</th>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Роль</th>
            <th>Департамент</th>
            <th>Выдан терминал</th>
            <th>Активен</th>
            <th>Дата создания</th>
            <th>Дата изменения</th>
            <th>Обновить профиль</th>
            <th>Удалить профиль</th>
        </tr>

        <c:forEach items="${listOfUsers}" var="eachUser">
            <tr class='row'>
                <td class='cell'>${eachUser.userLogin}</td>
                <td class='cell'>${eachUser.userName}</td>
                <td class='cell'>${eachUser.userSurname}</td>
                <td class='cell'>${eachUser.userRole}</td>
                <td class='cell'>${eachUser.userDepartment}</td>
                <td class='cell'>${eachUser.terminalRegId}</td>
                <td class='cell'>${eachUser.active == true? 'Да' : 'Нет'}</td>
                <td class='cell'>${eachUser.createDate}</td>
                <td class='cell'>${eachUser.lastUpdateDate}</td>

                <td class='cell'>
                    <c:if test="${eachUser.userRole != 'root' || role == 'root'}">

                    <form class="forms" method='post'
                          action="${pageContext.servletContext.contextPath}/selector">
                        <input type='hidden' name='id' value='${eachUser.id}'/>
                        <input type='hidden' name='login' value='${eachUser.userLogin}'/>
                        <input type='hidden' name='name' value='${eachUser.userName}'/>
                        <input type='hidden' name='surname' value='${eachUser.userSurname}'/>
                        <input type='hidden' name='role' value='${eachUser.userRole}'/>
                        <input type='hidden' name='department' value='${eachUser.userDepartment}'/>
                        <input type='hidden' name='isActive' value='${eachUser.active}'/>
                        <input type='hidden' name='listOfDeparts' value='${listOfDeparts}'/>
                        <input type='hidden' name='listOfRoles' value='${listOfRoles}'/>
                        <input type='hidden' name='action' value='update_user'/>
                        <input type='submit' value='Изменить'>
                    </form>
                    </c:if>
                </td>


                <td class='cell'>

                    <c:if test="${eachUser.userRole != 'root'}">
                        <form class="delete_forms" method='post' action="${pageContext.servletContext.contextPath}/deleteuser" >
                            <input type='hidden' name='id' value='${eachUser.id}'/>
                            <input type='hidden' name='terminalRegId' value='${eachUser.terminalRegId}'/>
                            <input type='hidden' name='loginToDelete' class="userToDelete" value='${eachUser.userLogin}'/>
                            <input type='hidden' name='action' value='delete_user'/>
                        </form>
                        <button class="deleteBtn" onclick="modalWin(this)">Удалить</button>
                    </c:if>
                </td>

            </tr>

        </c:forEach>

        <tr>
            <td class="table_footer">

                <form method='post' action="${pageContext.servletContext.contextPath}/selector">
                    <input type='hidden' name='action' value='add_user'/>
                    <input type='hidden' name='listOfDeparts' value='${listOfDeparts}'/>
                    <input type='hidden' name='listOfRoles' value='${listOfRoles}'/>
                    <input type='submit' value='Создать'>
                </form>

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
    var elements = document.querySelectorAll('.userToDelete')

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


</body>
</html>
