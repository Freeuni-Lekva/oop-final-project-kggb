<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Announcement" %>
<%@ page import="Models.Message" %>
<%@ page import="Models.Quiz" %>
<%@ page import="Models.QuizTakesHistory" %>
<%@ page import="Models.Friend" %>
<%@ page import="Models.Achievement" %>

<%
    List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    List<Quiz> popularQuizzes = (List<Quiz>) request.getAttribute("popularQuizzes");
    List<Quiz> recentQuizzes = (List<Quiz>) request.getAttribute("recentQuizzes");
    List<QuizTakesHistory> quizTakesHistory = (List<QuizTakesHistory>) request.getAttribute("quizTakesHistory");
    List<Quiz> createdQuizzes = (List<Quiz>) request.getAttribute("createdQuizzes");
    List<Friend> friends = (List<Friend>) request.getAttribute("friendsHistory");
    List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements");
%>

<jsp:include page="header.jsp" />

<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" href="css/frontPage.css">
</head>
<body>

<div class="page-layout">

    <div class="top-bar">
        <div class="messages-profile">
            <form action="profile" method="get" style="display: inline;">
                <button type="submit">View Profile</button>
            </form>

            <div class="messages-dropdown-container">
                <button id="messages-btn">📩 Messages</button>
                <div id="messages-dropdown" class="messages-dropdown hidden">
                    <% if (messages == null || messages.isEmpty()) { %>
                    <div class="message-item">You have no new messages.</div>
                    <% } else {
                        for (Message m : messages) { %>
                    <div class="message-item" style="border-bottom: 1px solid #ccc; padding: 10px;">
                        <strong><%= m.getTitle() %></strong><br/>
                        From: <a href="profile?username=<%= m.getSentFrom() %>"><%= m.getSentFrom() %></a><br/>
                        <span><%= m.getMessage() %></span>
                    </div>
                    <%  }
                    } %>
                </div>
            </div>
        </div>
    </div>


    <div class="left-sidebar">
        <h2>Announcements</h2>
        <% if (announcements == null || announcements.isEmpty()) { %>
        <div>No announcements yet.</div>
        <% } else {
            for (Announcement a : announcements) { %>
            <div>
                <a><%= a.getTitle() %></a><br/>
                <a><%= a.getMessage() %></a><br/>
                <a><%= a.getCreatedAt() %></a>
            </div>
        <div>
        </div>
        <%  }
        } %>
        <a href="announcements.jsp">View all →</a>
    </div>

    <div class="main-content">

        <div class="section">
            <h2>Popular Quizes</h2>
            <% if (popularQuizzes == null || popularQuizzes.isEmpty()) { %>
            <div>No popular quizzes yet.</div>
            <% } else {
                for (Quiz q : popularQuizzes) { %>
            <div><a href="quiz.jsp?id=<%= q.getId() %>"><%= q.getName() %></a></div>
            <%  }
            } %>
        </div>

        <div class="section">
            <h2>Recently Created Quizes</h2>
            <% if (recentQuizzes == null || recentQuizzes.isEmpty()) { %>
            <div>No recently created quizzes yet.</div>
            <% } else {
                for (Quiz q : recentQuizzes) { %>
            <div><a href="quiz.jsp?id=<%= q.getId() %>"><%= q.getName() %></a></div>
            <%  }
            } %>
        </div>

        <div class="section">
            <h2>Your Recent Quiz Activity</h2>
            <% if (quizTakesHistory == null || quizTakesHistory.isEmpty()) { %>
            <div>No recent quiz activity yet.</div>
            <% } else {
                for (QuizTakesHistory quizTake : quizTakesHistory) { %>
                <div>
                    You took <a href="quiz.jsp?id=<%= quizTake.getQuizId() %>"><%= quizTake.getQuizId() %></a> on <%= quizTake.getTimeTaken() %>
                </div>
            <%  }
            } %>
        </div>

        <div class="section">
            <h2>Your Created Quizzes</h2>
            <% if (createdQuizzes == null || createdQuizzes.isEmpty()) { %>
            <div>No created quizzes by you yet.</div>
            <% } else {
                for (Quiz q : popularQuizzes) { %>
            <div><a href="quiz.jsp?id=<%= q.getId() %>"><%= q.getName() %></a></div>
            <%  }
            } %>
        </div>

        <div class="section">
            <h2>Friends' Quiz Activity</h2>
            <% if (friends == null || friends.isEmpty()) { %>
            <div>No friends yet.</div>
            <% } else {
                for (Friend f : friends) { %>
            <div>
                <a href="user.jsp?username=<%= f.getSecondFriendUsername() %>"><%= f.getSecondFriendUsername() %></a> took quiz
            </div>
            <%  }
            } %>
        </div>


    </div>

    <div class="right-sidebar">
        <h2>Achievements</h2>
        <% if (achievements == null || achievements.isEmpty()) { %>
        <div>No achievements yet.</div>
        <% } else {
            for (Achievement ach : achievements) { %>
        <div>🏅 <%= ach.getName() %>: <%= ach.getDescription() %></div>
        <%  }
        } %>
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
