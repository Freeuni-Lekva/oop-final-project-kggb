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
    <title>Add Multiple Choice Question</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
</head>
<body>
<div class="create-quiz-container">
    <h1>Add Multiple Choice Question</h1>

    <form action="AddMultipleChoiceServlet" method="post">
        <input type="hidden" name="quizId" value="<%= quizId %>">

        <label for="question">Question:</label>
        <textarea id="question" name="question" rows="3" required></textarea>

        <label for="correctAnswer">Correct Answer:</label>
        <input type="text" id="correctAnswer" name="correctAnswer" required>

        <label for="choice1">Incorrect Choice 1:</label>
        <input type="text" id="choice1" name="choices" required>

        <label for="choice2">Incorrect Choice 2:</label>
        <input type="text" id="choice2" name="choices" required>

        <label for="choice3">Incorrect Choice 3:</label>
        <input type="text" id="choice3" name="choices" required>

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
