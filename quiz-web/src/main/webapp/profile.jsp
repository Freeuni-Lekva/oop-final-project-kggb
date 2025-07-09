<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Models.User" %>
<%@ page import="Models.Achievement" %>
<%@ page import="Models.QuizTakesHistory" %>
<%@ page import="Models.Quiz" %>
<%@ page import="java.util.List" %>

<%
    User profileUser = (User) request.getAttribute("profileUser");
    List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements");
    List<QuizTakesHistory> takenQuizzes = (List<QuizTakesHistory>) request.getAttribute("takenQuizzes");
    List<Quiz> createdQuizzes = (List<Quiz>) request.getAttribute("createdQuizzes");
    List<User> friends = (List<User>) request.getAttribute("friends");
%>
<jsp:include page="header.jsp" />

<html>
<head>
    <title><%= profileUser != null ? profileUser.getUsername() : "User" %>'s Profile</title>
    <link rel="stylesheet" href="css/frontPage.css" />
</head>
<body>

<h1><%= profileUser != null ? profileUser.getUsername() : "User" %>'s Profile</h1>

<div class="page-layout">

    <div class="top-bar">
        <h1><%= profileUser != null ? profileUser.getUsername() : "User" %>'s Profile</h1>
    </div>

    <div class="left-sidebar">
        <h2>Achievements</h2>
        <% if (achievements == null || achievements.isEmpty()) { %>
        <div>No achievements yet.</div>
        <% } else {
            for (Achievement a : achievements) { %>
        <div>🏅 <%= a.getName() %>: <%= a.getDescription() %></div>
        <%  }
        } %>
    </div>

    <div class="main-content">
        <div class="section">
            <h2>Taken Quizzes</h2>
            <% if (takenQuizzes == null || takenQuizzes.isEmpty()) { %>
            <div>No quizzes taken yet.</div>
            <% } else {
                for (QuizTakesHistory quizTake : takenQuizzes) { %>
                <div>
                    You took <a href="quiz.jsp?id=<%= quizTake.getQuizId() %>"><%= quizTake.getQuizId() %></a> on <%= quizTake.getTimeTaken() %>
                </div>

            <%  }
            } %>
        </div>

        <div class="section">
            <h2>Created Quizzes</h2>
            <% if (createdQuizzes == null || createdQuizzes.isEmpty()) { %>
            <div>No quizzes created yet.</div>
            <% } else {
                for (Quiz cq : createdQuizzes) { %>
            <div>
                <a href="quiz.jsp?id=<%= cq.getId() %>"><%= cq.getName() %></a><br/>
                <span><%= cq.getDescription() %></span>
            </div>
            <%  }
            } %>
        </div>
    </div>

    <div class="right-sidebar">
        <h2>Friend List</h2>
        <% if (friends == null || friends.isEmpty()) { %>
        <div>No friends yet.</div>
        <% } else {
            for (User friend : friends) { %>
        <div>
            <a href="profile?username=<%= friend.getUsername() %>"><%= friend.getUsername() %></a>
        </div>
        <%  }
        } %>
    </div>

</div>


</body>
</html>