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
            <a href="frontPage">QuizWebsite</a>
        </div>

        <div class="search-bar">
            <form action="search.jsp" method="get">
                <input type="text" name="query" placeholder="Search quizzes..." />
                <button type="submit">Search</button>
            </form>
        </div>

        <div class="nav-links">
            <span>Welcome, <a href="user.jsp?username=<%= username %>"><%= username %></a></span>
            |
            <a href="frontPage">Home</a>
            |
            <a href="profile.jsp">Profile</a>
            |
            <a href="logout.jsp">Log Out</a>
        </div>
    </div>
</div>
