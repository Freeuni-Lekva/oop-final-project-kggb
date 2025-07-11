<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 7/9/2025
  Time: 3:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" href="css/welcome.css">
    <link rel="icon" href="images/BRAINBUZZ.png">


</head>
<body>
<h1>CREATE AN ACCOUNT</h1>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
<p><%= error %></p>
<% } %>

<form action = "registration" method = "post">
    <label>Username:</label><br>
    <input type="text" name="username" required><br><br>
    <label>First Name:</label><br>
    <input type="text" name="firstName" required><br><br>
    <label>Last Name:</label><br>
    <input type="text" name="lastName" required><br><br>
    <label>Password:</label><br>
    <input type="password" name="password" required><br><br>
    <label>Profile Picture Url:</label><br>
    <input type="text" name="profilePicture" required><br><br>
    <button type="submit">Create An Account</button>

</form>
<img src="images/LetsGoBrain.png" style="max-height: 250px; width: auto;  margin: 20px auto;">

</body>
</html>
