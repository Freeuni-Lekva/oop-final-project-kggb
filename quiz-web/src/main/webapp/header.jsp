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
            <a href="frontPage">QuizWebsite</a><br><br>
        </div>

        <div class="nav-links">
            <span>Welcome, <a href="user.jsp?username=<%= username %>"><%= username %></a></span>
            |
            <a href="frontPage">Home</a>
            |
            <a href="profile.jsp">Profile</a>
            |
            <a href="logout.jsp">Log Out</a><br><br>
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
