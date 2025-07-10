<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/welcome.css">

</head>
<body>

<%
    String logout = request.getParameter("logout");
    if ("success".equals(logout)) {
%>
<p style="color: red; font-weight: bold; text-align: center; margin-top: 20px;">
    You have been successfully logged out.
</p>
<%
    }
%>

<h1>LOGIN</h1>
<form action="LoginServlet" method="post">
    <label for="username">Username:</label><br>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br><br>

    <button type="submit">Log In</button>
</form>
<img src="images/LetsGoBrain.png" style="max-height: 500px; width: auto;  margin: 20px auto;">

</body>
</html>
