package Servlets;

import DAOs.QuizDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String tags = request.getParameter("tags");

        boolean randomized = request.getParameter("randomOrder") != null;
        boolean multiPage = "multiPage".equals(request.getParameter("pageMode"));
        boolean immediateScore = request.getParameter("immediateCorrection") != null;
        boolean practiceMode = request.getParameter("practiceMode") != null;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String creator = (String) session.getAttribute("username");

        int id = 0;

        try {
            id = QuizDAO.createQuiz(title, category, description, creator,
                    null, randomized, multiPage, immediateScore);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to create quiz: " + e.getMessage());
            request.getRequestDispatcher("createQuiz.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("addQuestion.jsp?quizId=" + id);
    }
}
