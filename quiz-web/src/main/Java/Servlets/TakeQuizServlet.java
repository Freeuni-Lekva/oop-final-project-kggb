package Servlets;


import DAOs.*;
import Models.Question;
import Models.Quiz;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String quizIdstr = request.getParameter("quizId");
        if(quizIdstr == null || quizIdstr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing quizId.");
            return;
        }


        try {
            long quizId = Long.parseLong(quizIdstr);
            Quiz quiz = QuizDAO.getQuiz(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }

            List<Object> allQuestions = new ArrayList<>();
            allQuestions.addAll(MultipleChoiceQuestionDAO.getQuestionsByQuizId(quizId));
            allQuestions.addAll(TrueOrFalseQuestionDAO.getQuestionsByQuizId(quizId));
            allQuestions.addAll(PictureResponseQuestionDAO.getQuestionsByQuizId(quizId));
            allQuestions.addAll(FillInTheBlankQuestionDAO.getQuestionsByQuizId(quizId));

            if (quiz.isRandomized()) {
                Collections.shuffle(allQuestions);
            }

            request.setAttribute("quiz", quiz);
            request.setAttribute("questions", allQuestions);
            request.setAttribute("immediateScore", quiz.isImmediate_score());

            if (quiz.isMulti_page()) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("takeQuizMultiPage.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("takeQuizSinglePage.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
