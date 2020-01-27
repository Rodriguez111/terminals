<style><%@include file="/resources/css/main/main_menu.css"%></style>
<style><%@include file="/resources/css/main/main_page.css" %></style>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="shortcut icon" type="image/png" href="${pageContext.servletContext.contextPath}/resources/png/icon.png"/>
    <title>Главная: данные учета</title>

</head>
<body>


<div class="header_strip">
    <div class="menu_container">
        <div id="logo">Version: 1.0.3<br>Developed by:<br>Ruben Khodzhaev<br>Copyright 2019 ©<br>All Rights Reserved.</div>
        <ul id="main_menu">
            <li class="li" id="left_button"><a class="active_button" href="${pageContext.servletContext.contextPath}/main">Главная</a></li>
            <li class="li"><a class="common_button" href="${pageContext.servletContext.contextPath}/users">Пользователи</a></li>
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



<div class="filter">
    <button id="filter_on_button" onClick="filterOn()">Включить фильтр</button>
    <button id="filter_off_button" onClick="filterOff()">Выключить фильтр</button>
    <button id="apply_filter_button" onClick="applyFilter()">Применить фильтр</button>
    <a href="${pageContext.servletContext.contextPath}/main"><button id="reset_filter_button" >Сбросить фильтр</button></a>

</div>



<div class="main_block">
    <table class="main_table" id="myTable">

        <form method="post" action="${pageContext.servletContext.contextPath}/sortregs" id="sortform">
        <tr class="table_header">
            <th class="header_cell" ><div class="table_titles">№ терминала</div>

                <div class="triangles_block" id="sortByRegId" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>

            </th>
            <th class="header_cell" ><div class="table_titles">Логин</div>
                <div class="triangles_block" id="sortByLogin" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>
            </th>
            <th class="header_cell"><div class="table_titles">Фамилия и имя</div>
                <div class="triangles_block" id="sortByFullName" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>



            <th class="header_cell"><div class="table_titles">Кто выдал</div>
                <div class="triangles_block" id="sortByWhoGave" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>
            </th>
            <th class="header_cell"><div class="table_titles">Кто принял</div>
                <div class="triangles_block" id="sortByWhoReceived" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>
            </th>


            <th class="header_cell" ><div class="table_titles">Дата выдачи</div>
                <div class="triangles_block" id="sortByStartDate" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>
            </th>
            <th class="header_cell" ><div class="table_titles">Дата приема</div>
                <div class="triangles_block" id="sortByEndDate" >
                    <div class="two_triangles" onClick="makeSortDown(this)">
                        &#x25B2;<br>
                        &#x25BC;
                    </div>
                    <div class="triangle_down" onClick="makeSortUp(this)">
                        &#x25BC;
                    </div>
                    <div class="triangle_up" onClick="makeSortDown(this)">
                        &#x25B2;
                    </div>
                </div>
            </th>
        </tr>
            <input id="sortWhat" type="hidden" name="sortWhat" >
            <input id="sortDirection" type="hidden" name="sortDirection" >
            <c:set scope="session" var="listOfRegs" value="${listOfRegs}"/>

        </form>


        <form method="post" action="${pageContext.servletContext.contextPath}/filterregs" id="filterform">
            <tr class="table_filter">
                <td class="filter_cell" >
                    <input class="filter_text_input" type="text" name="regIdFilter"  AUTOCOMPLETE="off" />
                </td>
                <td class="filter_cell" ><input class="filter_text_input" type="text" name="loginFilter"  AUTOCOMPLETE="off" >
                </td>
                <td class="filter_cell"><input class="filter_text_input" type="text" name="fullNameFilter"  AUTOCOMPLETE="off"/>
                </td>
                <td class="filter_cell"><input class="filter_text_input" type="text" name="whoGaveFilter"  AUTOCOMPLETE="off"/>
                </td>
                <td class="filter_cell"><input class="filter_text_input" type="text" name="whoReceivedFilter"  AUTOCOMPLETE="off"/>
                </td>
                <td class="filter_cell" >
                    <div class="filter_date_input_block" >
                        <input class="filter_date_input" type="date" name="startDateFilterFrom"  AUTOCOMPLETE="off" />
                        <input class="filter_date_input" type="date" name="startDateFilterTo"  AUTOCOMPLETE="off"/>
                    </div>


                </td>
                <td class="filter_cell" >
                    <div class="filter_date_input_block" >
                        <input class="filter_date_input" type="date" name="endDateFilterFrom"  AUTOCOMPLETE="off"/><br>
                        <input class="filter_date_input" type="date" name="endDateFilterTo"  AUTOCOMPLETE="off"/>
                    </div>
                </td>

            </tr>

        </form>

        <c:forEach items="${listOfRegs}" var="eachEntry">
            <tr class="row">
                <td class="cell">${eachEntry.terminalRegistrationId}</td>
                <td class="cell">${eachEntry.userLogin}</td>
                <td class="cell">${eachEntry.userFullName}</td>
                <td class="cell">${eachEntry.whoGave}</td>
                <td class="cell">${eachEntry.whoReceived}</td>
                <td class="cell">${eachEntry.startDate}</td>
                <td class="cell">${eachEntry.endDate}</td>
            </tr>

        </c:forEach>
        <tr>
            <td class="table_footer" colspan='7'>

            </td>
        </tr>

    </table>

</div>


<script>
    var buttonOn = document.querySelector('#filter_on_button');
    var buttonOff = document.querySelector('#filter_off_button');
    var apply = document.querySelector('#apply_filter_button');
    var reset = document.querySelector('#reset_filter_button');
    var filterPanel = document.querySelector('.table_filter');

    buttonOn.addEventListener('click', function () { //При нажатии Включить фильтр.
        filterPanel.style.display = 'table-row';
        buttonOff.style.display = 'block';
        apply.style.display = 'block';
        buttonOn.style.display = 'none';
        reset.style.display = 'none';
    });

    buttonOff.addEventListener('click', function () { //При нажатии Вылючить фильтр.
        filterPanel.style.display = 'none';
        buttonOff.style.display = 'none';
        apply.style.display = 'none';
        buttonOn.style.display = 'block';
        reset.style.display = 'block';
    });
    apply.addEventListener('click', function () { //При нажатии Вылючить фильтр.
        var filterForm = document.getElementById('filterform');
        filterForm.submit();
    });

</script>

<script>
    var sortDirection = document.getElementById('sortDirection');
    var sortForm = document.getElementById('sortform');

    function makeSortDown(element) {
        var triangles_block = element.parentNode;
        sortDirection.value = "down";
        var triangle_down = triangles_block.querySelector('.triangle_down');
        var all_two_triangel_blocks = document.querySelectorAll('.two_triangles'); //получаем массив классов two_triangles
        var all_triangle_down_blocks = document.querySelectorAll('.triangle_down');
        var all_triangle_up_blocks = document.querySelectorAll('.triangle_up');

        for (var i = 0; i < all_two_triangel_blocks.length; i++) { //в цикле сбрасываем вид блока треугольников на показ двух треугольников
            all_two_triangel_blocks[i].style.display = 'block';
            all_triangle_down_blocks[i].style.display = 'none';
            all_triangle_up_blocks[i].style.display = 'none';
        }
        var sortWhat = document.getElementById('sortWhat');
        sortWhat.value = triangles_block.id;
        element.style.display = 'none';
        triangle_down.style.display = 'block';
        sortForm.submit();
    }

    function makeSortUp(element) {
        var triangles_block = element.parentNode;
        var triangle_up = triangles_block.querySelector('.triangle_up');
        sortDirection.value = "up";
        element.style.display = 'none';
        triangle_up.style.display = 'block';
        var sortWhat = document.getElementById('sortWhat');
        sortWhat.value = triangles_block.id;
        sortForm.submit();
    }
</script>


<script>
    var filterButtonsCondition = ${filterButtonsCondition};
    var twoTriangles = document.getElementsByClassName("two_triangles");
    var triangleDown = document.getElementsByClassName("triangle_down");
    var triangleUp = document.getElementsByClassName("triangle_up");

    window.onload = function() {
        for (var i = 0; i < twoTriangles.length; i++) {
          var buttonId = triangleDown[i].parentNode.id;

        if (filterButtonsCondition[buttonId] == "neutral")  {
            twoTriangles[i].style.display = 'block';
            triangleDown[i].style.display = 'none';
            triangleUp[i].style.display = 'none';
        } else if (filterButtonsCondition[buttonId] == "up")  {
                twoTriangles[i].style.display = 'none';
                triangleDown[i].style.display = 'none';
                triangleUp[i].style.display = 'block';
            } else if (filterButtonsCondition[buttonId] == "down")  {
            twoTriangles[i].style.display = 'none';
            triangleDown[i].style.display = 'block';
            triangleUp[i].style.display = 'none';
        }
        }    };

</script>

</body>
</html>