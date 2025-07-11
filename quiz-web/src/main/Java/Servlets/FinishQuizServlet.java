package Servlets;

import DAOs.MultipleChoiceQuestionDAO;
import DAOs.TrueOrFalseQuestionDAO;
import DAOs.PictureResponseQuestionDAO;
// import other question DAOs as needed

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/FinishQuizServlet")
public class FinishQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdStr = request.getParameter("quizId");
        if (quizIdStr == null) {
            request.setAttribute("error", "Missing quiz ID.");
            request.getRequestDispatcher("addQuestion.jsp").forward(request, response);
            return;
        }

        long quizId = Long.parseLong(quizIdStr);

        try {
            int totalQuestions = 0;

            totalQuestions += new MultipleChoiceQuestionDAO().getQuestionsByQuizId(quizId).size();
            totalQuestions += new TrueOrFalseQuestionDAO().getQuestionsByQuizId(quizId).size();
            totalQuestions += new PictureResponseQuestionDAO().getQuestionsByQuizId(quizId).size();


            response.sendRedirect("quizCreated.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error checking questions count: " + e.getMessage());
            request.getRequestDispatcher("addQuestion.jsp").forward(request, response);
        }
    }
}
