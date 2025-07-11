package Servlets;

import DAOs.QuizDAO;
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
import java.util.List;

@WebServlet("/SearchQuizServlet")
public class SearchQuizServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizName =  request.getParameter("quizName");
        List<Quiz> result = new ArrayList<>();
        if(quizName != null && !quizName.isEmpty()){
            QuizDAO quizDAO = new QuizDAO();
            try {
                result = QuizDAO.getSimilarQuizName(quizName, 10);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        request.setAttribute("result", result);
        request.setAttribute("quizName", quizName);
        RequestDispatcher dispatcher = request.getRequestDispatcher("searchResults.jsp");
        dispatcher.forward(request, response);
    }
}
