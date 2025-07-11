<%--
  Created by IntelliJ IDEA.
  User: lukabatilashvili
  Date: 11.07.25
  Time: 00:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp" />

<html>
<head>
    <title>Quiz Submitted</title>
    <link rel="stylesheet" href="css/quiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>
<div class="top-bar">
  <h2>Quiz Submitted</h2>
</div>

<div class="page-layout">
  <div class="main-content">
    <p>Your answers have been saved. You can review your results later.</p>
      <form action="frontPage" method="get" style="display: inline;">
          <button type="submit" style="background-color: #4da6ff; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
              Home
          </button>
      </form>
  </div>
</div>

</body>
</html>
