<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Quiz" %>

<jsp:include page="header.jsp" />

<%
    String category = (String) request.getAttribute("category");
    List<Quiz> results = (List<Quiz>) request.getAttribute("result");
%>

<html>
<head>
    <title>Category Search Results</title>
    <link rel="stylesheet" href="css/smth.css">
</head>
<body>

<div class="section" style="padding: 20px;">
    <h2>Quizzes in Category: "<%= category %>"</h2>

    <% if (results == null || results.isEmpty()) { %>
    <div>No quizzes found in this category.</div>
    <% } else {
        for (Quiz q : results) { %>
    <div style="margin: 10px 0;">
        <a href="TakeQuizServlet?quizId=<%= q.getId() %>"><%= q.getName() %></a>
    </div>
    <% } } %>
</div>

</body>
</html>
