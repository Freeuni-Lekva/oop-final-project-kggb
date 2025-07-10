package Servlets;

import DAOs.PictureResponseQuestionDAO;
import Models.PictureResponseQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddPictureResponseServlet")
public class AddPictureResponseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long quizId = Long.parseLong(request.getParameter("quizId"));
            String pictureUrl = request.getParameter("pictureUrl");
            String questionText = request.getParameter("question");
            String correctAnswer = request.getParameter("correctAnswer");
            int points = Integer.parseInt(request.getParameter("points"));

            int questionOrder = getNextQuestionOrder(quizId);

            PictureResponseQuestion question = new PictureResponseQuestion(
                    0, quizId, pictureUrl, questionText, correctAnswer, questionOrder, points
            );

            PictureResponseQuestionDAO dao = new PictureResponseQuestionDAO();
            dao.addQuestion(question);

            response.sendRedirect("addQuestion.jsp?quizId=" + quizId);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add picture response question.");
        }
    }

    private int getNextQuestionOrder(long quizId) {
        try {
            PictureResponseQuestionDAO dao = new PictureResponseQuestionDAO();
            return dao.getQuestionsByQuizId(quizId).size() + 1;
        } catch (SQLException e) {
            return 1;
        }
    }
}
