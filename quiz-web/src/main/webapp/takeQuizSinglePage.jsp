<%--
  Created by IntelliJ IDEA.
  User: lukabatilashvili
  Date: 10.07.25
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.*" %>
<%@ page import="java.util.Collections" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    List<Object> questions = (List<Object>) request.getAttribute("questions");
    boolean immediateScore = (Boolean) request.getAttribute("immediateScore");
%>

<jsp:include page="header.jsp" />

<html>
<head>
    <title>quiz.getName() %> - Take Quiz</title>
    <link rel="stylesheet" href="css/quiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">
</head>
<body>

<div class="top-bar">
    <div class="top-left">
        <h2>Taking Quiz: <%= quiz.getName() %></h2>
    </div>
</div>

<div class="page-layout">
    <div class="main-content">

        <div class="section">
            <h2>Description</h2>
            <p><%= quiz.getDescription() %></p>
            <p>Category: <%= quiz.getCategory() %></p>
            <p>Created by: <a href="profile?username=<%= quiz.getCreator() %>"><%= quiz.getCreator() %></a></p>
        </div>

        <div class="section">
            <h2>Instructions</h2>
            <ul>
                <li>Answer all questions below.</li>
                <% if (quiz.isRandomized()) { %>
                <li>Questions are in randomized order.</li>
                <% } %>
                <% if (immediateScore) { %>
                <li>You will see your score immediately after submission.</li>
                <% } %>
            </ul>
        </div>

        <form action="GradeQuizServlet" method="post" class="quiz-form">
            <input type="hidden" name="quizId" value="<%= quiz.getId() %>"/>
            <input type="hidden" name="immediateScore" value="<%= immediateScore %>"/>

            <% for (int i = 0; i < questions.size(); i++) {
                Question q = (Question)questions.get(i);
            %>
            <div class="question-block">
                <h3>Question <%= (i + 1) %>:</h3>
                <p>Points: <%=q.getPoints()%></p>
                <% if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                %>
                <p><%= mcq.getQuestion() %></p>
                <% List<String> choices = mcq.getChoices();
                    Collections.shuffle(choices);
                    for (String choice : choices) { %>
                <label>
                    <input type="radio" name="q<%=i%>" value="<%= choice %>"> <%= choice %>
                </label><br/>
                <% } %>

                <% } else if (q instanceof TrueFalseQuestion) {
                    TrueFalseQuestion tfq = (TrueFalseQuestion) q;
                %>
                <p><%= tfq.getQuestion() %></p>
                <label>
                    <input type="radio" name="q<%= i %>" value="true"> True
                </label>
                <label>
                    <input type="radio" name="q<%= i %>" value="false"> False
                </label>

                <% } else if (q instanceof FillInTheBlankQuestion) {
                    FillInTheBlankQuestion fib = (FillInTheBlankQuestion) q;
                %>
                <p><%= fib.getQuestion() %></p>
                <input type="text" name="q<%= i %>" class="input-text"/>

                <% } else if (q instanceof PictureResponseQuestion) {
                    PictureResponseQuestion prq = (PictureResponseQuestion) q;
                %>
                <img src="<%= prq.getPictureUrl() %>" alt="Question Image" class="question-image"/>
                <p><%= prq.getQuestion() %></p>
                <input type="text" name="q<%= i %>" class="input-text"/>
                <% } %>
            </div>
            <hr/>
            <% } %>
            <input type="hidden" name="totalQuestions" value="<%= questions.size() %>"/>
            <button type="submit" class="action-button submit-btn">✅ Submit Quiz</button>
        </form>
    </div>
</div>
</body>
</html>
