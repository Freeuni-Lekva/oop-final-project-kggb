<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<jsp:include page="header.jsp" />

<%
    Integer score = (Integer) session.getAttribute("score");
    Integer totalPoints = (Integer) session.getAttribute("totalPoints");
    String quizName = (String) session.getAttribute("quizName");

    if (score == null || totalPoints == null || quizName == null) {
%>
<div class="error-message">Error: Results not available.</div>
<a href="frontPage.jsp">Return to Home</a>
<%
        return;
    }

    session.removeAttribute("score");
    session.removeAttribute("totalPoints");
    session.removeAttribute("quizName");
%>

<html>
<head>
    <title>Results for <%= quizName %></title>
    <link rel="stylesheet" href="css/quiz.css" />
</head>
<body>
<div class="top-bar">
    <h2>Quiz Results: <%= quizName %></h2>
</div>

<div class="page-layout">
    <div class="main-content">
        <h3>Your Score: <%= score %> / <%= totalPoints %></h3>
        <a href="frontPage.jsp">Return to Home</a>
    </div>
</div>
</body>
</html>
