package Servlets;

import DAOs.*;
import Models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/frontPage")
public class FrontPageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        if(username == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try{
            List<Announcement> announcements = AnnouncementDAO.getAnnouncements();
            List<Quiz> popularQuizzes = QuizDAO.getPopularQuizzes(10);
            List<Quiz> recentQuizzes = QuizDAO.getQuizzes(10);
            List<QuizTakesHistory> quizTakesHistory = QuizTakesHistoryDAO.getAllTakesForUser(username);
            List<Quiz> createdQuizzes = QuizDAO.getQuizzesByCreator(username, 10);
            List<UserAchievement> achievements = UserAchievementDAO.getUserAchievements(username);
            List<Message> messages = MessageDAO.messagesSentToUser(username);
            List<QuizTakesHistory> friendsHistory = QuizTakesHistoryDAO.getRecentTakesByFriends(username);

            request.setAttribute("announcements", announcements);
            request.setAttribute("popularQuizzes", popularQuizzes);
            request.setAttribute("recentQuizzes", recentQuizzes);
            request.setAttribute("quizTakesHistory", quizTakesHistory);
            request.setAttribute("createdQuizzes", createdQuizzes);
            request.setAttribute("achievements", achievements);
            request.setAttribute("messages", messages);
            request.setAttribute("friendsHistory", friendsHistory);

            RequestDispatcher dispatcher = request.getRequestDispatcher("frontPage.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
