<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<jsp:include page="header.jsp" />

<html>
<head>
    <title>Add Question</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>
<div class="create-quiz-container">
    <h1>Add Question</h1>

    <%
        String quizId = request.getParameter("quizId");
        if (quizId == null) {
    %>
    <p>Error: No quiz ID provided.</p>
    <%
    } else {
    %>

    <form action="AddQuestionServlet" method="post">
        <input type="hidden" name="quizId" value="<%= quizId %>">

        <label for="questionType">Question Type:</label><br>
        <select name="questionType" id="questionType" required>
            <option value="">-- Select Type --</option>
            <option value="true_or_false">True or False</option>
            <option value="fill_in_the_blank">Fill in the Blank</option>
            <option value="multiple_choice">Multiple Choice</option>
            <option value="picture_response">Picture Response</option>
        </select><br><br>


        <button type="submit">Next</button>
    </form>

    <form action="FinishQuizServlet" method="post" style="margin-top:20px;">
        <input type="hidden" name="quizId" value="<%= quizId %>">
        <button type="submit">Finish Adding Questions</button>
    </form>

    <%
        }
    %>
</div>
</body>
</html>
