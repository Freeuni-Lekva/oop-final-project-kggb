package Servlets;


import DAOs.*;
import Models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String usernameToSearch = request.getParameter("username");
        if (usernameToSearch == null || usernameToSearch.isEmpty()) {
            usernameToSearch = username;
        }

        try {
            User profileUser = UserDAO.getUser(usernameToSearch);

            List<UserAchievement> achievements = UserAchievementDAO.getUserAchievements(usernameToSearch);
            List<QuizTakesHistory> takenQuizzes = QuizTakesHistoryDAO.getAllTakesForUser(usernameToSearch);
            List<Quiz> createdQuizzes = QuizDAO.getCreatedByUser(usernameToSearch);
            List<Friend> friends = FriendDAO.getFriendsOfUser(usernameToSearch);

            request.setAttribute("profileUser", profileUser);
            request.setAttribute("achievements", achievements);
            request.setAttribute("takenQuizzes", takenQuizzes);
            request.setAttribute("createdQuizzes", createdQuizzes);
            request.setAttribute("friends", friends);

            RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
}
}
