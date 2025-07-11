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
    <title>Add Picture Response Question</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>
<div class="create-quiz-container">
    <h1>Add Picture Response Question</h1>

    <form action="AddPictureResponseServlet" method="post">
        <input type="hidden" name="quizId" value="<%= quizId %>">

        <label for="pictureUrl">Image URL:</label>
        <input type="text" id="pictureUrl" name="pictureUrl" placeholder="https://..." required>

        <label for="question">Question:</label>
        <textarea id="question" name="question" rows="3" required></textarea>

        <label for="correctAnswer">Correct Answer:</label>
        <input type="text" id="correctAnswer" name="correctAnswer" required>

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
