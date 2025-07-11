<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.*" %>

<jsp:include page="header.jsp" />


<%
    Object question = request.getAttribute("question");
    Question question1 = (Question)question; 
    int questionIndex = (Integer) request.getAttribute("questionIndex");
    int totalQuestions = (Integer) request.getAttribute("totalQuestions");
    Quiz quiz = (Quiz) request.getAttribute("quiz");
%>

<html>
<head>
    <title>Taking Quiz: <%= quiz.getName() %></title>
    <link rel="stylesheet" href="css/frontPage.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">
</head>
<body>
<h2><%= quiz.getName() %> — Question <%= questionIndex + 1 %> of <%= totalQuestions %></h2>
<p>Points: <%=question1.getPoints()%></p>


<div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 20px 30px; border-radius: 8px;">

    <form action="TakeQuizMultiPageServlet" method="post">
        <input type="hidden" name="questionIndex" value="<%= questionIndex %>"/>
        <input type="hidden" name="quizId" value="<%= quiz.getId() %>" />

        <%
            if (question instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion q = (MultipleChoiceQuestion) question;
        %>
        <p><%= q.getQuestion() %></p>
        <%
            List<String> choices = q.getChoices();
            Collections.shuffle(choices);
            for (String choice : choices) {
        %>
        <label>
            <input type="radio" name="answer" value="<%= choice %>" required />
            <%= choice %>
        </label><br/>
        <%
            }
        %>
        <%
        } else if (question instanceof TrueFalseQuestion) {
            TrueFalseQuestion q = (TrueFalseQuestion) question;
        %>
        <p><%= q.getQuestion() %></p>
        <label><input type="radio" name="answer" value="true" required /> True</label><br/>
        <label><input type="radio" name="answer" value="false" required /> False</label><br/>
        <%
        } else if (question instanceof FillInTheBlankQuestion) {
            FillInTheBlankQuestion q = (FillInTheBlankQuestion) question;
        %>
        <p><%= q.getQuestion() %></p>
        <input type="text" name="answer" required />
        <%
        } else if (question instanceof PictureResponseQuestion) {
            PictureResponseQuestion q = (PictureResponseQuestion) question;
        %>
        <p><%= q.getQuestion() %></p>
        <img src="<%= q.getpictureUrl() %>" alt="Question Image" width="300"/><br/>
        <input type="text" name="answer" required />
        <%
        } else {
        %>
        <p>Unknown question type.</p>
        <%
            }
        %>

        <br/><br/>
        <button type="submit">
            <%= (questionIndex + 1 == totalQuestions) ? "Submit Quiz" : "Next Question" %>
        </button>
    </form>
</div>
</body>
</html>
