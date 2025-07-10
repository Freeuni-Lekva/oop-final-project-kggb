package Servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/AddQuestionServlet")
public class AddQuestionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizId = request.getParameter("quizId");
        String questionType = request.getParameter("questionType");

        if (quizId == null || questionType == null) {
            request.setAttribute("error", "Missing quiz ID or question type.");
            request.getRequestDispatcher("addQuestion.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("quizId", quizId);
        session.setAttribute("questionType", questionType);

        String redirectAt;
        switch (questionType) {
            case "true_or_false":
                redirectAt = "addTF.jsp?quizId=" + quizId;
                break;
            case "fill_in_the_blank":
                redirectAt = "addFillInTheBlank.jsp?quizId=" + quizId;
                break;
            case "multiple_choice":
                redirectAt = "addMultipleChoice.jsp?quizId=" + quizId;
                break;
            case "picture_response":
                redirectAt = "addPictureResponse.jsp?quizId=" + quizId;
                break;
            default:
                request.setAttribute("error", "Invalid question type selected.");
                request.getRequestDispatcher("addQuestion.jsp").forward(request, response);
                return;
        }

        response.sendRedirect(redirectAt);
    }
}
