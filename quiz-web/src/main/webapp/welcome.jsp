<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" href="css/welcome.css">
</head>
<body>

<!-- Logo Image -->
<img src="images/brainbuzz-logo.png" alt="BrainBuzz Logo" style="width:200px; display:block; margin: 20px auto;">

<h1>Welcome to <span class="brand">BrainBuzz</span></h1>
<p class="instruction">Log in or create an account to access quizzes</p>

<form action="login.jsp" method="get">
    <button type="submit">LOG IN</button>
</form>

<form action="registration.jsp" method="get">
    <button type="submit">CREATE AN ACCOUNT</button>
</form>

</body>
</html>
