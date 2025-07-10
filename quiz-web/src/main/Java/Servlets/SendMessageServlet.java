package Servlets;

import DAOs.MessageDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sendMessage")
public class SendMessageServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        String fromUser = (String) session.getAttribute("username");
        String toUser =  request.getParameter("toUser");
        String message =  request.getParameter("message");
        String title = request.getParameter("title");

        try{
            MessageDAO.addMessage(fromUser, toUser, message, title);
            response.sendRedirect("profile?username=" + toUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
