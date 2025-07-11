package Servlets;
import DAOs.*;
import Models.Question;
import Models.Quiz;
import Models.QuizTakesHistory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GradeQuizServlet")
public class GradeQuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpSession session = request.getSession();

            long quizId = Long.parseLong(request.getParameter("quizId"));
            boolean immediateScore = Boolean.parseBoolean(request.getParameter("immediateScore"));

            List<Question> questions = (List<Question>) session.getAttribute("questionsForGrading");

            int score = 0;
            int maxScore = 0;
            for (int i = 0; i < questions.size(); i++) {
                Question q =  questions.get(i);
                String userAnswer = request.getParameter("q" + i);
                maxScore += q.getPoints();
                if (q.isUsersAnswerCorrect(userAnswer)) {
                    score += q.getPoints();
                }
            }

            String username = (String) request.getSession().getAttribute("username");
            if (username != null) {
                QuizTakesHistory take = new QuizTakesHistory(0, username, quizId, score, maxScore, null, null);
                new QuizTakesHistoryDAO().addQuizTake(take);
            }

            Quiz quiz = QuizDAO.getQuiz(quizId);

            if (immediateScore) {
                session.setAttribute("score", score);
                session.setAttribute("totalPoints", maxScore);
                session.setAttribute("quizName", quiz.getName());
                request.getSession().setAttribute("quizId", quiz.getId());
                request.getRequestDispatcher("quizResults.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("quizSubmitted.jsp").forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }
}