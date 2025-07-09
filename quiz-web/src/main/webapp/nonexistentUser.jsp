<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Not Found</title>
    <link rel="stylesheet" href="css/welcome.css">
</head>
<body>
<h2 class="error-message">User does not exist</h2>

<form action="login.jsp" method="get" class="inline-form">
    <button type="submit">Go Back to Log In</button>
</form>

<form action="registration.jsp" method="get" class="inline-form">
    <button type="submit">Create an Account</button>
</form>
</body>
</html>
