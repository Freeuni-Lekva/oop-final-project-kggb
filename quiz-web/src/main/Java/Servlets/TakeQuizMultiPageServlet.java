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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/TakeQuizMultiPageServlet")
public class TakeQuizMultiPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdStr = request.getParameter("quizId");
        String questionIndexStr = request.getParameter("q");

        if (quizIdStr == null || quizIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing quizId.");
            return;
        }

        int questionIndex = 0;
        if (questionIndexStr != null) {
            try {
                questionIndex = Integer.parseInt(questionIndexStr);
            } catch (NumberFormatException e) {
                questionIndex = 0;
            }
        }

        try {
            long quizId = Long.parseLong(quizIdStr);
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

            if (questionIndex < 0) questionIndex = 0;
            if (questionIndex >= allQuestions.size()) questionIndex = allQuestions.size() - 1;

            Object currentQuestion = allQuestions.get(questionIndex);

            request.setAttribute("quiz", quiz);
            request.setAttribute("question", currentQuestion);
            request.setAttribute("questionIndex", questionIndex);
            request.setAttribute("totalQuestions", allQuestions.size());

            request.getSession().setAttribute("multiPageQuestions", allQuestions);
            request.getSession().setAttribute("quizId", quizId);

            request.getRequestDispatcher("takeQuizMultiPage.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Object> allQuestions = (List<Object>) session.getAttribute("multiPageQuestions");
        Long quizId = (Long) session.getAttribute("quizId");

        if (allQuestions == null || quizId == null) {
            response.sendRedirect("frontPage.jsp");
            return;
        }

        int questionIndex = Integer.parseInt(request.getParameter("questionIndex"));
        String answer = request.getParameter("answer");

        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("multiPageAnswers");
        if (answers == null) {
            answers = new HashMap<>();
        }
        answers.put(questionIndex, answer);
        session.setAttribute("multiPageAnswers", answers);

        int nextQuestionIndex = questionIndex + 1;

        if (nextQuestionIndex >= allQuestions.size()) {
            session.removeAttribute("multiPageQuestions");
            session.removeAttribute("multiPageAnswers");
            session.removeAttribute("quizId");

            response.sendRedirect("quizResults.jsp?quizId=" + quizId);
        } else {
            response.sendRedirect("TakeQuizMultiPageServlet?quizId=" + quizId + "&q=" + nextQuestionIndex);
        }
    }
}
