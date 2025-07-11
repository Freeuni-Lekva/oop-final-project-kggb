package Servlets;

import DAOs.*;
import Models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/frontPage")
public class FrontPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Announcement> announcements = AnnouncementDAO.getAnnouncements();
            List<Quiz> popularQuizzes = QuizDAO.getPopularQuizzes(10);
            List<Quiz> recentQuizzes = QuizDAO.getQuizzes(10);
            List<QuizTakesHistory> quizTakesHistory = QuizTakesHistoryDAO.getAllTakesForUser(username);
            List<Quiz> createdQuizzes = QuizDAO.getQuizzesByCreator(username, 10);
            List<UserAchievement> achievements = UserAchievementDAO.getUserAchievements(username);
            List<Message> messages = MessageDAO.messagesSentToUser(username);
            List<QuizTakesHistory> friendsHistory = QuizTakesHistoryDAO.getRecentTakesByFriends(username);
            List<Challenge> challenges = ChallengeDAO.getAllChallenges(); // Or create a filter method for this user
            Map<Long, String> quizIdToName = new HashMap<>();
            for (QuizTakesHistory h : quizTakesHistory) {
                long quizId = h.getQuizId();
                if (!quizIdToName.containsKey(quizId)) {
                    Quiz quiz = QuizDAO.getQuiz(quizId);
                    if (quiz != null) {
                        quizIdToName.put(quizId, quiz.getName());
                    }
                }
            }
            for (Quiz q : createdQuizzes) {
                long quizId = q.getId();
                quizIdToName.putIfAbsent(quizId, q.getName());
            }
            for (QuizTakesHistory h : friendsHistory) {
                long quizId = h.getQuizId();
                if (!quizIdToName.containsKey(quizId)) {
                    Quiz quiz = QuizDAO.getQuiz(quizId);
                    if (quiz != null) {
                        quizIdToName.put(quizId, quiz.getName());
                    }
                }
            }
            request.setAttribute("announcements", announcements);
            request.setAttribute("popularQuizzes", popularQuizzes);
            request.setAttribute("recentQuizzes", recentQuizzes);
            request.setAttribute("quizTakesHistory", quizTakesHistory);
            request.setAttribute("createdQuizzes", createdQuizzes);
            request.setAttribute("achievements", achievements);
            request.setAttribute("messages", messages);
            request.setAttribute("friendsHistory", friendsHistory);

            request.setAttribute("challenges", challenges);
            request.setAttribute("quizIdToName", quizIdToName);

            RequestDispatcher dispatcher = request.getRequestDispatcher("frontPage.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
