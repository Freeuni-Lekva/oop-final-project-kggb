package Servlets;

import DAOs.*;
import Models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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

            HttpSession session = request.getSession();
            session.setAttribute("multiPageQuestions", allQuestions);
            session.setAttribute("quizId", quizId);

            request.getRequestDispatcher("takeQuizMultiPage.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Long quizId = (Long) session.getAttribute("quizId");
        if (quizId == null) {
            try {
                quizId = Long.parseLong(request.getParameter("quizId"));
                session.setAttribute("quizId", quizId);
            } catch (NumberFormatException e) {
                response.sendRedirect("frontPage.jsp");
                return;
            }
        }

        List<Object> allQuestions = (List<Object>) session.getAttribute("multiPageQuestions");
        if (allQuestions == null) {
            try {
                allQuestions = new ArrayList<>();
                allQuestions.addAll(MultipleChoiceQuestionDAO.getQuestionsByQuizId(quizId));
                allQuestions.addAll(TrueOrFalseQuestionDAO.getQuestionsByQuizId(quizId));
                allQuestions.addAll(PictureResponseQuestionDAO.getQuestionsByQuizId(quizId));
                allQuestions.addAll(FillInTheBlankQuestionDAO.getQuestionsByQuizId(quizId));
                session.setAttribute("multiPageQuestions", allQuestions);
            } catch (SQLException e) {
                response.sendRedirect("frontPage.jsp");
                return;
            }
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
            int score = 0;
            int totalPoints = 0;

            for (int i = 0; i < allQuestions.size(); i++) {
                Object q = allQuestions.get(i);
                String userAnswer = answers.get(i);

                if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mc = (MultipleChoiceQuestion) q;
                    totalPoints += mc.getPoints();
                    if (mc.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                        score += mc.getPoints();
                    }

                } else if (q instanceof TrueFalseQuestion) {
                    TrueFalseQuestion tf = (TrueFalseQuestion) q;
                    totalPoints += tf.getPoints();
                    if (Boolean.toString(tf.isCorrectAnswer()).equalsIgnoreCase(userAnswer)) {
                        score += tf.getPoints();
                    }

                } else if (q instanceof FillInTheBlankQuestion) {
                    FillInTheBlankQuestion fib = (FillInTheBlankQuestion) q;
                    totalPoints += fib.getPoints();
                    boolean match = fib.isCaseSensitive() ?
                            fib.getCorrectAnswer().equals(userAnswer) :
                            fib.getCorrectAnswer().equalsIgnoreCase(userAnswer);
                    if (match) {
                        score += fib.getPoints();
                    }

                } else if (q instanceof PictureResponseQuestion) {
                    PictureResponseQuestion pr = (PictureResponseQuestion) q;
                    totalPoints += pr.getPoints();
                    if (pr.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                        score += pr.getPoints();
                    }
                }
            }

            String quizName = "Quiz";
            try {
                Quiz quiz = QuizDAO.getQuiz(quizId);
                if (quiz != null) {
                    quizName = quiz.getName();
                }
            } catch (SQLException ignored) {}

            session.setAttribute("score", score);
            session.setAttribute("totalPoints", totalPoints);
            session.setAttribute("quizName", quizName);

            session.removeAttribute("multiPageQuestions");
            session.removeAttribute("quizId");

            response.sendRedirect("quizResults.jsp?quizId=" + quizId);

        } else {
            response.sendRedirect("TakeQuizMultiPageServlet?quizId=" + quizId + "&q=" + nextQuestionIndex);
        }
    }
}
