package Servlets;

import DAOs.FriendDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/removeFriend")
public class RemoveFriendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String user1 = (String) session.getAttribute("username");
        String user2 = request.getParameter("friendUsername");

        try {
            FriendDAO.removeFriends(user1, user2); 
            response.sendRedirect("profile?username=" + user2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
