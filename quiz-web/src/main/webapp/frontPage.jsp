<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="Models.*" %>
<%@ page import="DAOs.QuizDAO" %>

<%
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    List<Quiz> popularQuizzes = (List<Quiz>) request.getAttribute("popularQuizzes");
    List<Quiz> recentQuizzes = (List<Quiz>) request.getAttribute("recentQuizzes");
    List<QuizTakesHistory> quizTakesHistory = (List<QuizTakesHistory>) request.getAttribute("quizTakesHistory");
    List<Quiz> createdQuizzes = (List<Quiz>) request.getAttribute("createdQuizzes");
    List<QuizTakesHistory> friendsHistory = (List<QuizTakesHistory>) request.getAttribute("friendsHistory");
    List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<jsp:include page="header.jsp" />

<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" href="css/smth.css">
    <link rel="icon" href="images/BRAINBUZZ.png">
</head>
<body>

<div class="top-bar">
    <form action="createQuiz.jsp" method="get">
        <button type="submit" class="action-button create-quiz-btn">➕ Create a Quiz</button>
    </form>

    <div class="messages-dropdown-container">
        <button id="messages-btn" class="action-button message-btn">📩 Messages</button>
        <div id="messages-dropdown" class="messages-dropdown hidden">
            <% if (messages == null || messages.isEmpty()) { %>
            <div class="message-item">You have no new messages.</div>
            <% } else {
                for (Message m : messages) { %>
            <div class="message-item">
                <strong><%= m.getTitle() %></strong><br/>
                From: <a href="profile?username=<%= m.getSentFrom() %>"><%= m.getSentFrom() %></a><br/>
                <span><%= m.getMessage() %></span>
            </div>
            <% } } %>
        </div>
    </div>
</div>

<div class="page-layout">

    <div class="left-content">
        <div class="section">
            <h2>Popular Quizzes</h2>
            <% if (popularQuizzes == null || popularQuizzes.isEmpty()) { %>
            <div>No popular quizzes yet.</div>
            <% } else {
                for (Quiz q : popularQuizzes) { %>
            <div><a href="QuizSummaryServlet?quizId=<%= q.getId() %>"><%= q.getName() %></a></div>
            <% } } %>
        </div>

        <div class="section">
            <h2>Recently Created Quizzes</h2>
            <% if (recentQuizzes == null || recentQuizzes.isEmpty()) { %>
            <div>No recently created quizzes yet.</div>
            <% } else {
                for (Quiz q : recentQuizzes) { %>
            <div><a href="QuizSummaryServlet?quizId=<%= q.getId() %>"><%= q.getName() %></a></div>
            <% } } %>
        </div>
    </div>

    <div class="center-content">
        <div class="section">
            <h2>Your Recent Quiz Activity</h2>
            <% if (quizTakesHistory == null || quizTakesHistory.isEmpty()) { %>
            <div>No recent quiz activity yet.</div>
            <% } else {
                for (QuizTakesHistory quizTake : quizTakesHistory) {
                    Quiz q = QuizDAO.getQuiz(quizTake.getQuizId());
                    String title = (q != null) ? q.getName() : "Quiz " + quizTake.getQuizId();
            %>
            <div>
                You took <a href="QuizSummaryServlet?quizId=<%= quizTake.getQuizId() %>"><%= title %></a> on <%= dateFormat.format(quizTake.getTimeTaken()) %>
            </div>
            <% } } %>
        </div>

        <div class="section">
            <h2>Your Created Quizzes</h2>
            <% if (createdQuizzes == null || createdQuizzes.isEmpty()) { %>
            <div>No created quizzes by you yet.</div>
            <% } else {
                for (Quiz q : createdQuizzes) { %>
            <div><a href="QuizSummaryServlet?quizId=<%= q.getId() %>"><%= q.getName() %></a></div>
            <% } } %>
        </div>

        <div class="section">
            <h2>Friends' Quiz Activity</h2>
            <% if (friendsHistory == null || friendsHistory.isEmpty()) { %>
            <div>No quiz activity from friends yet.</div>
            <% } else {
                for (QuizTakesHistory quizTake : friendsHistory) {
                    Quiz q = QuizDAO.getQuiz(quizTake.getQuizId());
                    String title = (q != null) ? q.getName() : "Quiz " + quizTake.getQuizId();
            %>
            <div>
                <a href="user.jsp?username=<%= quizTake.getUsername() %>"><%= quizTake.getUsername() %></a> took
                <a href="QuizSummaryServlet?quizId=<%= quizTake.getQuizId() %>"><%= title %></a> on
                <%= dateFormat.format(quizTake.getTimeTaken()) %>
            </div>
            <% } } %>
        </div>
    </div>

    <div class="right-sidebar">
        <h2>Achievements</h2>
        <% if (achievements == null || achievements.isEmpty()) { %>
        <div>No achievements yet.</div>
        <% } else {
            for (Achievement ach : achievements) { %>
        <div>🏅 <%= ach.getName() %>: <%= ach.getDescription() %></div>
        <% } } %>
        <a href="achievements.jsp">View all →</a>
    </div>

</div>

<script>
    const msgBtn = document.getElementById('messages-btn');
    const dropdown = document.getElementById('messages-dropdown');

    msgBtn.addEventListener('click', () => {
        dropdown.classList.toggle('hidden');
    });

    document.addEventListener('click', (e) => {
        if (!msgBtn.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.classList.add('hidden');
        }
    });
</script>

</body>
</html>
