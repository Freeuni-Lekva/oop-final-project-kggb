package Servlets;

import DAOs.UserDAO;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String profilePicture = request.getParameter("profilePicture");

        try{
            if(UserDAO.userExists(username)){
                request.setAttribute("error", "Username already exists. Choose a new username.");
                request.getRequestDispatcher("registration.jsp").forward(request, response);

            }else{
                UserDAO.createUser(username, firstName, lastName, password, profilePicture);
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("registration.jsp").forward(request, response);

        }
    }
}
