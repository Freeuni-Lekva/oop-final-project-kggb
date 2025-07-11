<%--
  Created by IntelliJ IDEA.
  User: lukabatilashvili
  Date: 10.07.25
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  String toUser = request.getParameter("toUser");
  String fromUser = (String) session.getAttribute("username");
  if (fromUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>

<html>
<head>
    <title>Send Message</title>
    <link rel="stylesheet" href="css/welcome.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">


</head>
<body>
    <h2>Send Message to <%= toUser %></h2>

    <form action="sendMessage" method="post">
      <input type="hidden" name="toUser" value="<%= toUser %>">
      <label for="title">Title:</label><br/>
      <input type="text" id="title" name="title" required><br/><br/>

      <label>Message:</label><br>
      <textarea name="message" rows="5" cols="40" required></textarea><br><br>
      <button type="submit">Send Message</button>
    </form>

    <a href="profile?username=<%= toUser %>">Back to Profile</a>
</body>
</html>
