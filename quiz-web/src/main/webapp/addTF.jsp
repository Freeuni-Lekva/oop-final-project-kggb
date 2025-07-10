<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<jsp:include page="header.jsp" />

<%
    String quizId = request.getParameter("quizId");
    if (quizId == null) {
%>
<div class="error-message">Error: Missing quiz ID.</div>
<%
        return;
    }
%>

<html>
<head>
    <title>Add True/False Question</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
</head>
<body>
<div class="create-quiz-container">
    <h1>Add True/False Question</h1>

    <form action="AddTFServlet" method="post">
        <input type="hidden" name="quizId" value="<%= quizId %>">

        <label for="question">Question:</label>
        <textarea id="question" name="question" rows="3" required></textarea>

        <label for="answer">Correct Answer:</label>
        <select id="answer" name="answer" required>
            <option value="true">True</option>
            <option value="false">False</option>
        </select>

        <label for="points">Points:</label>
        <input type="number" id="points" name="points" min="1" value="1" required>

        <button type="submit">Add Question</button>
    </form>

    <form action="addQuestion.jsp" method="get">
        <input type="hidden" name="quizId" value="<%= quizId %>">
        <button type="submit">Back</button>
    </form>
</div>
</body>
</html>
