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

<div id="header">
    <div class="header-content">
        <div class="logo">
            <a href="frontPage">BrainBuzz</a><br><br>
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
                <button type="submit" style="background-color: #ff3333; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                    Sign Out
                </button>
            </form>
            <br><br>
        </div>


        <div class="search-bar">
            <form action="search.jsp" method="get" class="quiz-search">
                <input type="text" name="query" placeholder="Search quiz" />
                <button type="submit">Search</button>
            </form>

            <form action="profile" method="get" class="user-search">
                <input type="text" name="username" placeholder="Search user" required>
                <button type="submit">Search</button>
            </form>
        </div>

    </div>
</div>
