<%--
  Created by IntelliJ IDEA.
  User: lukabatilashvili
  Date: 11.07.25
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Models.*" %>
<%@ page import="java.util.*" %>
<jsp:include page="header.jsp" />

<%
  Quiz quiz = (Quiz) request.getAttribute("quiz");
  List<Question> questions = (List<Question>) request.getAttribute("questions");
%>

<html>
<head>
    <title>Correct Answers for <%= quiz.getName()%></title>
    <link rel="stylesheet" href="css/quiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">
</head>
<body>

<div class="top-bar">
  <h2>Correct Answers: <%= quiz.getName()%></h2>
</div>

<div class="page-layout">
  <div class="main-content">
    <% for(int i = 0; i < questions.size(); i++){
      Question q = questions.get(i);
      %>
    <div class="question-block">
      <h3>Question <%=(i + 1)%> : <%=q.getQuestion()%></h3>
      <% if (q instanceof MultipleChoiceQuestion) {
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
      %>
      <p><strong>Correct Answer:</strong> <%= mcq.getCorrectAnswer() %></p>
      <% } else if (q instanceof TrueFalseQuestion) {
        TrueFalseQuestion tfq = (TrueFalseQuestion) q;
      %>
      <p><strong>Correct Answer:</strong> <%= tfq.isCorrectAnswer() %></p>
      <% } else if (q instanceof FillInTheBlankQuestion) {
        FillInTheBlankQuestion fibq = (FillInTheBlankQuestion) q;
      %>
      <p><strong>Correct Answer:</strong> <%= fibq.getCorrectAnswer() %></p>
      <% } else if (q instanceof PictureResponseQuestion) {
        PictureResponseQuestion prq = (PictureResponseQuestion) q;
      %>
      <img src="<%= prq.getPictureUrl() %>" alt="Question Image" class="question-image"/>
      <p><strong>Correct Answer:</strong> <%= prq.getCorrectAnswer() %></p>
      <% } %>
    </div>
    <% } %>

    <form action="frontPage" method="get">
      <button type="submit" style="background-color: #4da6ff; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
        Home
      </button>
    </form>
  </div>

</div>

</body>
</html>
