<%--
  Created by IntelliJ IDEA.
  User: lukabatilashvili
  Date: 11.07.25
  Time: 00:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>

<%
    Integer score = (Integer) request.getAttribute("score");
    Integer totalPoints = (Integer) request.getAttribute("totalPoints");
    String quizName = (String) request.getAttribute("quizName");
%>
<jsp:include page="header.jsp" />
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
        <a href="index.jsp">Return to Home</a>

    </div>
</div>

</body>
</html>
