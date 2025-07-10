package Servlets;

import DAOs.FillInTheBlankQuestionDAO;
import Models.FillInTheBlankQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AddFillInTheBlankServlet")
public class AddFillInTheBlankServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            long quizId = Long.parseLong(request.getParameter("quizId"));
            String questionText = request.getParameter("questionText");
            String correctAnswer = request.getParameter("correctAnswer");
            String caseSensitiveParam = request.getParameter("caseSensitive");
            boolean caseSensitive = "on".equalsIgnoreCase(caseSensitiveParam); // checkbox

            if (questionText == null || correctAnswer == null || correctAnswer.trim().isEmpty()) {
                response.sendRedirect("addFillInTheBlank.jsp?quizId=" + quizId + "&error=Missing+fields");
                return;
            }

            FillInTheBlankQuestion question = new FillInTheBlankQuestion();
            question.setQuizId(quizId);
            question.setQuestion(questionText.trim());
            question.setCorrectAnswer(correctAnswer.trim());
            question.setCaseSensitive(caseSensitive);
            question.setPoints(1);
            question.setQuestionOrder(0);

            FillInTheBlankQuestionDAO fillInTheBlankQuestionDAO = new FillInTheBlankQuestionDAO();
            fillInTheBlankQuestionDAO.addQuestion(question);
            response.sendRedirect("addQuestion.jsp?quizId=" + quizId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addFillInTheBlank.jsp?quizId=" + request.getParameter("quizId") + "&error=Server+error");
        }
    }
}

