package Servlets;

import DAOs.*;
import Models.Question;
import Models.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ShowCorrectAnswersServlet")
public class ShowCorrectAnswersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            long quizId = Long.parseLong(request.getParameter("quizId"));
            Quiz quiz = QuizDAO.getQuiz(quizId);

            List<Question> questions = new ArrayList<>();
            questions.addAll(MultipleChoiceQuestionDAO.getQuestionsByQuizId(quizId));
            questions.addAll(TrueOrFalseQuestionDAO.getQuestionsByQuizId(quizId));
            questions.addAll(FillInTheBlankQuestionDAO.getQuestionsByQuizId(quizId));
            questions.addAll(PictureResponseQuestionDAO.getQuestionsByQuizId(quizId));

            request.setAttribute("quiz", quiz);
            request.setAttribute("questions", questions);

            request.getRequestDispatcher("showCorrectAnswers.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
