<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Quiz" %>

<jsp:include page="header.jsp" />

<%
  String quizName = (String) request.getAttribute("quizName");
  List<Quiz> results = (List<Quiz>) request.getAttribute("result");
%>

<html>
<head>
  <title>Search Results</title>
  <link rel="stylesheet" href="css/smth.css">
</head>
<body>

<div class="section" style="max-width: 600px; margin: 0 auto; background-color: white; padding: 20px 30px; border-radius: 8px;">
  <h2>Search Results for Quiz: "<%= quizName %>"</h2>

  <% if (results == null || results.isEmpty()) { %>
  <div>No quizzes found.</div>
  <% } else {
    for (Quiz q : results) { %>
  <div style="margin: 10px 0;">
    <a href="QuizSummaryServlet?quizId=<%= q.getId() %>"><%= q.getName() %></a>
  </div>
  <% } } %>
</div>

</body>
</html>
