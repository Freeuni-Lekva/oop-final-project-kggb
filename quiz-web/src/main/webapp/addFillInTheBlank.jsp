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
    <title>Add Fill in the Blank Question</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
</head>
<body>
<div class="create-quiz-container">
    <h1>Add Fill-in-the-Blank Question</h1>

    <form action="AddFillInTheBlankServlet" method="post">
        <input type="hidden" name="quizId" value="<%= quizId %>">
        <input type="hidden" name="questionType" value="fill_in_the_blank">

        <label for="questionText">Question Text:</label>
        <textarea id="questionText" name="questionText" rows="3" required></textarea>

        <label for="correctAnswer">Correct Answer:</label>
        <input type="text" id="correctAnswer" name="correctAnswer" required>

        <label>
            <input type="checkbox" name="caseSensitive">
            Case sensitive answer
        </label>

        <button type="submit">Add Question</button>
    </form>

    <form action="addQuestion.jsp" method="get">
        <input type="hidden" name="quizId" value="<%= quizId %>">
        <button type="submit">Back</button>
    </form>
</div>
</body>
</html>
