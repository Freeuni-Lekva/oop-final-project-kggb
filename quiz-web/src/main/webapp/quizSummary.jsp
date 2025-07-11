<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Models.*, java.util.*, java.text.DecimalFormat, java.text.SimpleDateFormat" %>
<jsp:include page="header.jsp" />

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    List<QuizTakesHistory> userAttempts = (List<QuizTakesHistory>) request.getAttribute("userAttempts");
    List<QuizTakesHistory> topAllTime = (List<QuizTakesHistory>) request.getAttribute("topAllTime");
    List<QuizTakesHistory> topToday = (List<QuizTakesHistory>) request.getAttribute("topToday");
    List<QuizTakesHistory> recentAttempts = (List<QuizTakesHistory>) request.getAttribute("recentAttempts");
    double avg = (double) request.getAttribute("avgScore");
    double min = (double) request.getAttribute("minScore");
    double max = (double) request.getAttribute("maxScore");
    int totalAttempts = (int) request.getAttribute("totalAttempts");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DecimalFormat df = new DecimalFormat("#.##");
%>

<html>
<head>
    <title><%= quiz.getName() %> Summary</title>
    <link rel="stylesheet" href="css/smth.css">
</head>
<body>

<div class="quiz-summary-container">

    <h1><%= quiz.getName() %></h1>
    <p><strong>Description:</strong> <%= quiz.getDescription() %></p>
    <p><strong>Category:</strong> <%= quiz.getCategory() %></p>
    <p><strong>Created on:</strong> <%= quiz.getCreation_date() %></p>
    <p><strong>Created by:</strong> <a href="user.jsp?username=<%= quiz.getCreator() %>"><%= quiz.getCreator() %></a></p>

    <form action="TakeQuizServlet" method="get">
        <input type="hidden" name="quizId" value="<%= quiz.getId() %>">
        <button type="submit">🚀 Start Quiz</button>
    </form>

    <hr/>

    <h2>Your Past Attempts</h2>
    <% if (userAttempts.isEmpty()) { %>
    <p>No attempts yet.</p>
    <% } else { %>
    <table border="1">
        <tr><th>Date</th><th>Score</th><th>Time Spent</th></tr>
        <% for (QuizTakesHistory h : userAttempts) { %>
        <tr>
            <td><%= sdf.format(h.getTimeTaken()) %></td>
            <td><%= h.getScore() %> / <%= h.getMaxScore() %></td>
            <td><%= h.getTimeSpent() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <hr/>

    <h2>Top Performers (All Time)</h2>
    <% if (topAllTime.isEmpty()) { %>
    <p>No scores yet.</p>
    <% } else { %>
    <table border="1">
        <tr><th>User</th><th>Score</th><th>Date</th></tr>
        <% for (QuizTakesHistory h : topAllTime) { %>
        <tr>
            <td><a href="user.jsp?username=<%= h.getUsername() %>"><%= h.getUsername() %></a></td>
            <td><%= h.getScore() %> / <%= h.getMaxScore() %></td>
            <td><%= sdf.format(h.getTimeTaken()) %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <h2>Top Performers (Last 24h)</h2>
    <% if (topToday.isEmpty()) { %>
    <p>No scores in the last day.</p>
    <% } else { %>
    <table border="1">
        <tr><th>User</th><th>Score</th><th>Date</th></tr>
        <% for (QuizTakesHistory h : topToday) { %>
        <tr>
            <td><a href="user.jsp?username=<%= h.getUsername() %>"><%= h.getUsername() %></a></td>
            <td><%= h.getScore() %> / <%= h.getMaxScore() %></td>
            <td><%= sdf.format(h.getTimeTaken()) %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <hr/>

    <h2>Recent Attempts</h2>
    <% if (recentAttempts.isEmpty()) { %>
    <p>No recent attempts yet.</p>
    <% } else { %>
    <table border="1">
        <tr><th>User</th><th>Score</th><th>Date</th></tr>
        <% for (QuizTakesHistory h : recentAttempts) { %>
        <tr>
            <td><a href="user.jsp?username=<%= h.getUsername() %>"><%= h.getUsername() %></a></td>
            <td><%= h.getScore() %> / <%= h.getMaxScore() %></td>
            <td><%= sdf.format(h.getTimeTaken()) %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <hr/>

    <h2>Quiz Stats</h2>
    <p><strong>Total Attempts:</strong> <%= totalAttempts %></p>
    <p><strong>Average Score:</strong> <%= df.format(avg) %>%</p>
    <p><strong>Highest Score:</strong> <%= df.format(max) %>%</p>
    <p><strong>Lowest Score:</strong> <%= df.format(min) %>%</p>

</div>

</body>
</html>
