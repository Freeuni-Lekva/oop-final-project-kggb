package Servlets;

import DAOs.FriendRequestDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/removeFriendRequest")
public class RemoveFriendRequestServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String fromUser = (String) session.getAttribute("username");
        String toUser = request.getParameter("toUser");
        try {
            FriendRequestDAO.removeFriendRequest(fromUser, toUser);
            response.sendRedirect("profile?username=" + toUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
