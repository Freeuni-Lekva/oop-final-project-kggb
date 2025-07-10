package Servlets;

import DAOs.TrueOrFalseQuestionDAO;
import Models.TrueFalseQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddTFServlet")
public class AddTFServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long quizId = Long.parseLong(request.getParameter("quizId"));
            String questionText = request.getParameter("question");
            String answerStr = request.getParameter("answer");
            int points = Integer.parseInt(request.getParameter("points"));

            boolean correctAnswer = Boolean.parseBoolean(answerStr);

            int questionOrder = getNextQuestionOrder(quizId);
            TrueFalseQuestion question = new TrueFalseQuestion(
                    0, quizId, questionText, correctAnswer, questionOrder, points
            );

            TrueOrFalseQuestionDAO dao = new TrueOrFalseQuestionDAO();
            dao.addQuestion(question);

            response.sendRedirect("addQuestion.jsp?quizId=" + quizId);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add question.");
        }
    }

    private int getNextQuestionOrder(long quizId) {
        try {
            TrueOrFalseQuestionDAO dao = new TrueOrFalseQuestionDAO();
            return dao.getQuestionsByQuizId(quizId).size() + 1;
        } catch (SQLException e) {
            return 1;
        }
    }
}
