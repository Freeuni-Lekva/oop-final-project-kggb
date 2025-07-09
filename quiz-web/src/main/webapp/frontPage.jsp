<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                    <c:if test="${empty messages}">
                        <div class="message-item">You have no new messages.</div>
                    </c:if>
                    <c:forEach var="m" items="${messages}">
                        <div class="message-item">
                                ${m.messageType} from
                            <a href="user.jsp?username=${m.sender}">${m.sender}</a><br/>
                                ${m.shortContent}
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <div class="left-sidebar">
        <h2>Announcements</h2>
        <c:forEach var="a" items="${announcements}">
            <div>
                <strong>${a.title}</strong><br/>
                    ${a.content}<br/>
                <span>${a.date}</span>
            </div>
        </c:forEach>
    </div>

    <div class="main-content">
        <div class="section">
            <h2>Popular Quizzes</h2>
            <c:forEach var="quiz" items="${popularQuizzes}">
                <div><a href="quiz.jsp?id=${quiz.id}">${quiz.title}</a></div>
            </c:forEach>
        </div>

        <div class="section">
            <h2>Recently Created Quizzes</h2>
            <c:forEach var="quiz" items="${recentQuizzes}">
                <div><a href="quiz.jsp?id=${quiz.id}">${quiz.title}</a></div>
            </c:forEach>
        </div>

        <div class="section">
            <h2>Your Recent Quiz Activity</h2>
            <c:forEach var="take" items="${quizTakesHistory}">
                <div>You took <a href="quiz.jsp?id=${take.quizId}">${take.quizId}</a> on ${take.timeTaken}</div>
            </c:forEach>
        </div>

        <c:if test="${not empty createdQuizzes}">
            <div class="section">
                <h2>Your Created Quizzes</h2>
                <c:forEach var="quiz" items="${createdQuizzes}">
                    <div><a href="quiz.jsp?id=${quiz.id}">${quiz.title}</a></div>
                </c:forEach>
            </div>
        </c:if>

        <div class="section">
            <h2>Friends' Quiz Activity</h2>
            <c:forEach var="f" items="${friendsHistory}">
                <div>
                    <a href="user.jsp?username=${f.username}">${f.username}</a> took quiz
                    <a href="quiz.jsp?id=${f.quizId}">${f.quizId}</a> on ${f.timeTaken}
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="right-sidebar">
        <h2>Your Achievements</h2>
        <c:forEach var="a" items="${achievements}">
            <div>🏅 ${a.achievementName}: ${a.description}</div>
        </c:forEach>
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
