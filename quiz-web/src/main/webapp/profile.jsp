<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="Models.*" %>
<%@ page import="DAOs.QuizDAO" %>

<%
    String loggedInUsername = (String)session.getAttribute("username");
    User profileUser = (User) request.getAttribute("profileUser");
    List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements");
    List<QuizTakesHistory> takenQuizzes = (List<QuizTakesHistory>) request.getAttribute("takenQuizzes");
    List<Quiz> createdQuizzes = (List<Quiz>) request.getAttribute("createdQuizzes");
    List<Friend> friends = (List<Friend>) request.getAttribute("friends");

    String error = (String) request.getAttribute("error");

    Boolean isFriend = (Boolean) request.getAttribute("isFriend");
    Boolean requestSentByLoggedInUser = (Boolean) request.getAttribute("requestSentByLoggedInUser");
    Boolean requestSentToLoggedInUser = (Boolean) request.getAttribute("requestSentToLoggedInUser");
    String profileUsername = profileUser != null ? profileUser.getUsername() : "";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<jsp:include page="header.jsp" />

<html>
<head>
    <title><%= profileUser != null ? profileUser.getUsername() : "User" %>'s Profile</title>
    <link rel="stylesheet" href="css/frontPage.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>

<% if (error != null) { %>
<div class="error-message" style="margin: 15px;">
    <%= error %>
</div>
<% } %>

<% if (profileUser != null) { %>
<h1><%= profileUser.getUsername() %>'s Profile</h1>

<div class="page-layout">
    <div class="main-content" style="max-width: 600px; margin: 0 auto; background-color: white; padding: 20px 30px; border-radius: 8px;">
        <div class="section">
            <h2>Profile Info</h2>
            <p><strong>Username:</strong> <%= profileUser.getUsername() %></p>
            <p><strong>First Name:</strong> <%= profileUser.getFirst_name() %></p>
            <p><strong>Last Name:</strong> <%= profileUser.getLast_name() %></p>

            <% if (profileUser.getProfile_picture() != null && !profileUser.getProfile_picture().isEmpty()) { %>
            <img src="<%= profileUser.getProfile_picture() %>" alt="Profile Photo" style="max-width:150px;">
            <% } %>

            <% if (!loggedInUsername.equals(profileUsername)) { %>
            <div style="margin-top: 10px;">
                <% if (isFriend != null && isFriend) { %>
                <span>Friends</span>
                <form action="removeFriend" method="post" style="display:inline;">
                    <input type="hidden" name="friendUsername" value="<%= profileUsername %>">
                    <button type="submit">Remove Friend</button>
                </form>
                <% } else if (requestSentByLoggedInUser != null && requestSentByLoggedInUser) { %>
                <span>Friend Request Sent</span>
                <form action="removeFriendRequest" method="post" style="display:inline;">
                    <input type="hidden" name="toUser" value="<%= profileUsername %>">
                    <button type="submit">Remove Request</button>
                </form>
                <% } else if (requestSentToLoggedInUser != null && requestSentToLoggedInUser) { %>
                <form action="approveFriendRequest" method="post" style="display:inline;">
                    <input type="hidden" name="fromUser" value="<%= profileUsername %>">
                    <input type="hidden" name="toUser" value="<%= loggedInUsername %>">
                    <button type="submit">Approve Friend Request</button>
                </form>
                <form action="rejectFriendRequest" method="post" style="display:inline;">
                    <input type="hidden" name="fromUser" value="<%= profileUsername %>">
                    <button type="submit">Reject</button>
                </form>
                <% } else { %>
                <form action="sendFriendRequest" method="post" style="display:inline;">
                    <input type="hidden" name="toUser" value="<%= profileUsername %>">
                    <button type="submit">Send Friend Request</button>
                </form>
                <% } %>

                <form action="sendMessage.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="toUser" value="<%= profileUsername %>">
                    <button type="submit">Send Message</button>
                </form>
            </div>
            <% } %>
        </div>

        <div class="section">
            <h2>Achievements</h2>
            <% if (achievements == null || achievements.isEmpty()) { %>
            <div>No achievements yet.</div>
            <% } else {
                for (Achievement a : achievements) { %>
            <div>🏅 <%= a.getName() %>: <%= a.getDescription() %></div>
            <%  } } %>
        </div>

        <div class="section">
            <h2>Taken Quizzes</h2>
            <% if (takenQuizzes == null || takenQuizzes.isEmpty()) { %>
            <div>No quizzes taken yet.</div>
            <% } else {
                for (QuizTakesHistory quizTake : takenQuizzes) {
                    Quiz quiz = QuizDAO.getQuiz(quizTake.getQuizId());
                    String title = (quiz != null) ? quiz.getName() : "Quiz " + quizTake.getQuizId();
            %>
            <div>
                Took <a href="QuizSummaryServlet?quizId=<%= quizTake.getQuizId() %>"><%= title %></a><br/>
                Score: <%= quizTake.getScore() %><br/>
                Date: <%= dateFormat.format(quizTake.getTimeTaken()) %>
            </div>
            <% } } %>
        </div>

        <div class="section">
            <h2>Created Quizzes</h2>
            <% if (createdQuizzes == null || createdQuizzes.isEmpty()) { %>
            <div>No quizzes created yet.</div>
            <% } else {
                for (Quiz cq : createdQuizzes) { %>
            <div>
                <a href="QuizSummaryServlet?quizId=<%= cq.getId() %>"><%= cq.getName() %></a><br/>
                <span><%= cq.getDescription() %></span>
            </div>
            <%  } } %>
        </div>

        <div class="section">
            <h2>Friend List</h2>
            <% if (friends == null || friends.isEmpty()) { %>
            <div>No friends yet.</div>
            <% } else {
                for (Friend friend : friends) {
                    String otherUsername;
                    if (friend.getFirstFriendUsername().equals(profileUsername)) {
                        otherUsername = friend.getSecondFriendUsername();
                    } else {
                        otherUsername = friend.getFirstFriendUsername();
                    }
            %>
            <div>
                <a href="profile?username=<%= otherUsername %>"><%= otherUsername %></a>
            </div>
            <%  } } %>
        </div>
    </div>
</div>
<% } %>

</body>
</html>
