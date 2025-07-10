package Servlets;

import DAOs.MultipleChoiceQuestionDAO;
import Models.MultipleChoiceQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AddMultipleChoiceServlet")
public class AddMultipleChoiceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            long quizId = Long.parseLong(request.getParameter("quizId"));
            String questionText = request.getParameter("question");
            String correctAnswer = request.getParameter("correctAnswer");
            String[] incorrectChoices = request.getParameterValues("choices");
            int points = Integer.parseInt(request.getParameter("points"));

            if (questionText == null || correctAnswer == null || incorrectChoices == null || incorrectChoices.length < 3) {
                response.sendRedirect("addMultipleChoice.jsp?quizId=" + quizId + "&error=Missing+fields");
                return;
            }

            List<String> allChoices = new ArrayList<>();
            allChoices.add(correctAnswer);
            for (String choice : incorrectChoices) {
                allChoices.add(choice.trim());
            }

            MultipleChoiceQuestion question = new MultipleChoiceQuestion();
            question.setQuizId(quizId);
            question.setQuestion(questionText);
            question.setCorrectAnswer(correctAnswer.trim());
            question.setChoices(allChoices);
            question.setPoints(points);
            question.setQuestionOrder(0);

            MultipleChoiceQuestionDAO dao = new MultipleChoiceQuestionDAO();
            dao.addQuestion(question);

            response.sendRedirect("addQuestion.jsp?quizId=" + quizId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addMultipleChoice.jsp?quizId=" + request.getParameter("quizId") + "&error=Server+error");
        }
    }
}
