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

@WebServlet("/QuizSummaryServlet")
public class QuizSummaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String quizIdStr = request.getParameter("quizId");
        if (quizIdStr == null || quizIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing quizId.");
            return;
        }

        try {
            long quizId = Long.parseLong(quizIdStr);
            Quiz quiz = QuizDAO.getQuiz(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }

            String currentUser = (String) request.getSession().getAttribute("username");

            // Fetch required data
            List<QuizTakesHistory> userAttempts = QuizTakesHistoryDAO.getUserAttemptsOnQuiz(currentUser, quizId);
            List<QuizTakesHistory> topAllTime = QuizTakesHistoryDAO.getTopPerformersAllTime(quizId);
            List<QuizTakesHistory> topToday = QuizTakesHistoryDAO.getTopPerformersToday(quizId);
            List<QuizTakesHistory> recentAttempts = QuizTakesHistoryDAO.getRecentAttempts(quizId);

            double avgScore = QuizTakesHistoryDAO.getAverageScore(quizId);
            double maxScore = QuizTakesHistoryDAO.getMaxScore(quizId);
            double minScore = QuizTakesHistoryDAO.getMinScore(quizId);
            int totalAttempts = QuizTakesHistoryDAO.getTotalAttempts(quizId);

            // Pass all data to JSP
            request.setAttribute("quiz", quiz);
            request.setAttribute("userAttempts", userAttempts);
            request.setAttribute("topAllTime", topAllTime);
            request.setAttribute("topToday", topToday);
            request.setAttribute("recentAttempts", recentAttempts);
            request.setAttribute("avgScore", avgScore);
            request.setAttribute("maxScore", maxScore);
            request.setAttribute("minScore", minScore);
            request.setAttribute("totalAttempts", totalAttempts);

            RequestDispatcher dispatcher = request.getRequestDispatcher("quizSummary.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quizId.");
        }
    }
}
