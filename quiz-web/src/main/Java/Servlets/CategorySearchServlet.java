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
import java.util.ArrayList;
import java.util.List;


@WebServlet("/CategorySearchServlet")
public class CategorySearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
        List<Quiz> result = new ArrayList<>();

        if(category != null && !category.isEmpty()){
            QuizDAO quizDAO = new QuizDAO();
            result = QuizDAO.getQuizzesByCategory(category);
        }

        request.setAttribute("result", result);
        request.setAttribute("category", category);
        RequestDispatcher dispatcher = request.getRequestDispatcher("categorySearchResult.jsp");
        dispatcher.forward(request, response);
    }
}
