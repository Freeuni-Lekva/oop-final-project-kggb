package Servlets;

import DAOs.FriendDAO;
import DAOs.FriendRequestDAO;
import DAOs.MessageDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/approveFriendRequest")
public class ApproveFriendRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        String toUser = request.getParameter("toUser");
        String fromUser = request.getParameter("fromUser");

        if (fromUser == null || fromUser.isEmpty()) {
            response.sendRedirect("profile?username=" + toUser);
            return;
        }

        try {
            FriendDAO.addFriends(fromUser, toUser);

            FriendRequestDAO.removeFriendRequest(fromUser, toUser);

            String title = "Friend Request Accepted";
            String message = toUser + " accepted your friend request.";
            MessageDAO.addMessage(toUser, fromUser, message, title);

            response.sendRedirect("profile?username=" + fromUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
