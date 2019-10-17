<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/departs/departs_main.css" %></style>
<style><%@include file="/resources/css/departs/delete_popup.css" %></style>
<style><%@include file="/resources/css/departs/depart_error_popup.css" %></style>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Департаменты</title>


</head>
<body>

<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Version: 1.0.1<br>Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
        <ul id="main_menu">
            <li class="li" id="left_button"><a class="common_button" href="${pageContext.servletContext.contextPath}/main">Главная</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/users">Пользователи</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/terminals">Терминалы</a></li>
            <li class="li"><a class="active_button" href="${pageContext.servletContext.contextPath}/departs">Департаменты</a></li>
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
            <th>Название департамента</th>
            <th id="updateColumn">Редактировать</th>
            <th id="deleteColumn">Удалить</th>

        </tr>

        <c:forEach items="${listOfDepartments}" var="eachDepartment">
            <tr class='row'>
                <td class='department_name_cell'>${eachDepartment}</td>
                <td class='cell' style="text-align: center">
                    <button class="updateBtn" onclick="update(this)">Переименовать</button>
                </td>

                <td class='cell' style="text-align: center">
                    <form class="delete_forms" method='post'
                          action="${pageContext.servletContext.contextPath}/deletedepart">
                        <input type='hidden' name='department' class="departmentToDelete" value='${eachDepartment}'/>
                        <input type='hidden' name='action' value='delete_depart'/>
                    </form>
                    <button class="deleteBtn" onclick="modalWin(this)">Удалить</button>
                </td>

            </tr>

        </c:forEach>

        <tr>
            <td id="table_footer" class="table_footer">
                <form method='post' action="${pageContext.servletContext.contextPath}/selector">
                    <input type='hidden' name='action' value='add_depart'/>
                    <input type='submit' value='Создать'>
                </form>

                <div id="update_container">
                    Новое название:
                    <input id="renameInput" type="text"/>
                    <button id="okRenameButton" >Ok</button>
                    <button id="cancelRenameButton">Отменить</button>
                </div>
                <div id="info_container">
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

    var departToRename;
    var cellWithDepartToUpdate;
    var departNewName;
    function update(element) {
        clearSelection();
        clearUpdateContainer();
        showRenameDialog(element);
      departToRename =  getDepartNameForRenaming(element);
    }

    function getDepartNameForRenaming(element) {
       var arrayOfUpdateButtons = document.getElementsByClassName("updateBtn");
       var counterOfButtons = 0;
       for (var i = 0; i < arrayOfUpdateButtons.length; i++) {
           if (element === arrayOfUpdateButtons[i]) {
               counterOfButtons = i;
               break;
           }
       }
      var arrayOfDepartmentNameCells = document.getElementsByClassName("department_name_cell");
      cellWithDepartToUpdate = arrayOfDepartmentNameCells[counterOfButtons];
      return arrayOfDepartmentNameCells[counterOfButtons].innerHTML;
    }


    function showRenameDialog(element) {
      var updateContainer = document.getElementById("update_container");
      var row = element.parentElement.parentElement;
      row.style.backgroundColor = "#abb5c0";
      updateContainer.style.display = "block";
      var inputField = document.getElementById("renameInput");
      inputField.focus();
    }

    function clearSelection() {
        var allRows = document.getElementsByClassName("row");
        for (var i = 0; i < allRows.length; i++) {
            allRows[i].style.backgroundColor = "";
        }
    }

    function clearUpdateContainer() {
        var inputField = document.getElementById("renameInput");
        inputField.value = "";
        var updateContainer = document.getElementById("update_container");
        updateContainer.style.display = "none";
        var infoContainer = document.getElementById("info_container");
        infoContainer.innerHTML = "";
    }


   var cancelRenameButton = document.getElementById("cancelRenameButton");
    cancelRenameButton.addEventListener("click", function () {
        clearSelection();
        clearUpdateContainer();

    });

    var okRenameButton = document.getElementById("okRenameButton");
    okRenameButton.addEventListener("click", function () {
        var newDepName = document.getElementById("renameInput").value;
        if(validate(newDepName)) {
            departNewName = newDepName;
            renameDepartment();
            clearUpdateContainer();
        }
    });



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


    function renameDepartment() {
        var jsonObj = JSON.stringify({"renameDepartment":departToRename, "newDepartmentName":departNewName});
        sendAjaxRequest(jsonObj, displayNewDepartmentName)
    }

    function displayNewDepartmentName(data) {
        var message = data.result;
        var infoContainer = document.getElementById("info_container");
        if(message !== "OK") {
            infoContainer.innerHTML = message;
            clearSelection();
        }  else {
            cellWithDepartToUpdate.innerHTML = departNewName;
            clearUpdateContainer();
            clearSelection();
        }
    }
    
    function validate(inputString) {
        var infoContainer = document.getElementById("info_container");
        var result = true;
        if (inputString === '') {
            result = false;
            infoContainer.innerHTML = 'Название не может быть пустым';
        } else if (!validateLength(inputString, 3, 30)) {
            result = false;
            infoContainer.innerHTML = 'Название должно быть от 3 до 30 символов и не содержать пробелы';
        } else if (inputString === departToRename) {
            result = false;
            infoContainer.innerHTML = 'Это то же самое название';
        }
        return result;
    }

    function validateLength(string, minLength, maxLength) {
        return string.length >= minLength && string.length <= maxLength && !string.includes(' ')  && !string.includes('\t');
    }

        





</script>









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
    var elements = document.querySelectorAll('.departmentToDelete');

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
        var depart = elements[count].getAttribute("value");
        var infoBlock = document.getElementById('modal-body');
        infoBlock.innerHTML = 'Вы действительно хотите удалить департамент <b>' + depart + '</b>?';


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
