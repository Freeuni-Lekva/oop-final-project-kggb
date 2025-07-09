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
</head>
<body>
<h1>CREATE AN ACCOUNT</h1>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
<p><%= error %></p>
<% } %>

<form action = "registration" method = "post">
    <label>Username:</label>
    <input type="text" name="username" required><br>
    <label>First Name:</label>
    <input type="text" name="firstName" required><br>
    <label>Last Name:</label>
    <input type="text" name="lastName" required><br>
    <label>Password:</label>
    <input type="password" name="password" required><br>
    <label>Profile Picture Url:</label>
    <input type="text" name="profilePicture" required><br>
    <button type="submit">Create An Account</button>

</form>
</body>
</html>
