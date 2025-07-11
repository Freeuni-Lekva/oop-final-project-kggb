<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<jsp:include page="header.jsp" />

<%
    Integer score = (Integer) request.getAttribute("score");
    Integer totalPoints = (Integer) request.getAttribute("totalPoints");
    String quizName = (String) request.getAttribute("quizName");


%>



<html>
<head>
    <title>Results for <%= quizName %></title>
    <link rel="stylesheet" href="css/quiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>
<div class="top-bar">
    <h2>Quiz Results: <%= quizName %></h2>
</div>

<div class="page-layout">
    <div class="main-content">
        <h3>Your Score: <%= score %> / <%= totalPoints %></h3>
        <form action="frontPage" method="get" style="display: inline;">
            <button type="submit" style="background-color: #4da6ff; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                Home
            </button>
        </form>
    </div>
</div>
</body>
</html>
