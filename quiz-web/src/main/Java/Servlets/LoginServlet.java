package Servlets;

import DAOs.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        UserDAO userDAO = new UserDAO();
        try {
            if (!userDAO.userExists(username)) {
                response.sendRedirect("nonexistentUser.jsp");
                return;
            } else if (userDAO.correctPassword(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                if(UserDAO.isAdmin(username)) {
                    session.setAttribute("isAdmin", true);
                }else{
                    session.setAttribute("isAdmin", false);
                }
                response.sendRedirect("frontPage.jsp");
                return;
            } else {
                response.sendRedirect("wrongPassword.jsp");
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
