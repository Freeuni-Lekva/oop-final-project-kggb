<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp" />

<html>
<head>
  <title>Quiz Created Successfully</title>
  <link rel="stylesheet" href="css/createQuiz.css" />
</head>
<body>
<div class="create-quiz-container" style="text-align:center;">
  <h1>Success!</h1>
  <p>Your quiz has been successfully created and all questions have been added.</p>

  <form action="frontPage.jsp" method="get">
    <button type="submit">Go Back to Homepage</button>
  </form>
</div>
</body>
</html>
