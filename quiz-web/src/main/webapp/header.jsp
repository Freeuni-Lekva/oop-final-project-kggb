<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<link rel="stylesheet" href="css/welcome.css" />
<link rel="icon" href="images/BRAINBUZZ.png">


<div id="header">
    <div class="header-content" style="display: flex; align-items: center; justify-content: space-between; padding: 10px 20px; max-width: 1200px; margin: 0 auto;">
        <div class="logo">
            <img src="images/BRAINBUZZ.png" style="height: 80px; width: auto;">
            <a href="frontPage" style="font-size: 28px; color: #3498db; font-weight: bold;">BrainBuzz</a>
        </div>

        <div class="search-bar" style="text-align: right">
            <form action="search.jsp" method="get" class="quiz-search" style="display: flex; align-items: center; gap: 5px;">
                <input type="text" name="query" placeholder="Search quiz" style="width: 200px; padding: 5px;" />
                <button type="submit" style="padding: 5px 10px;">Search</button>
            </form>

            <form action="profile" method="get" class="user-search" style="display: flex; align-items: center; gap: 5px;">
                <input type="text" name="username" placeholder="Search user" required style="width: 200px; padding: 5px;" />
                <button type="submit" style="padding: 5px 10px;">Search</button>
            </form>
        </div>

        <div class="nav-links">
            <span>WELCOME BACK, <%= username %></span>
            |
            <form action="frontPage" method="get" style="display: inline;">
                <button type="submit" style="background-color: #4da6ff; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                    Home
                </button>
            </form>
            |
            <form action="profile" method="get" style="display: inline;">
                <button type="submit" style="background-color: #4da6ff; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                    Profile
                </button>
            </form>
            |
            <form action="LogoutServlet" method="get" style="display: inline;">
                <button type="submit" style="background-color: #dc5252; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                    Sign Out
                </button>
            </form>
            <br><br>
        </div>

    </div>
</div>
