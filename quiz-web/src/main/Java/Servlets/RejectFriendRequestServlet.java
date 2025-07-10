package Servlets;

import DAOs.FriendRequestDAO;
import DAOs.MessageDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/rejectFriendRequest")
public class RejectFriendRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String toUser = (String) session.getAttribute("username");
        String fromUser = request.getParameter("fromUser");
        try {
            FriendRequestDAO.removeFriendRequest(fromUser, toUser);
            String title = "Friend Request Rejected";
            String message = toUser + " rejected your friend request.";
            MessageDAO.addMessage(toUser, fromUser, message, title);
            response.sendRedirect("profile?username=" + fromUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
