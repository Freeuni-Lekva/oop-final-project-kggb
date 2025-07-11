package Servlets;

import DAOs.ChallengeDAO;
import DAOs.FriendDAO;
import DAOs.QuizDAO;
import Models.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/SendChallengeServlet")
public class SendChallengeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String challenger = (session != null) ? (String) session.getAttribute("username") : null;
        if (challenger == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String challenged = request.getParameter("challengedUser");
        String quizName = request.getParameter("quizName");
        String challengeMessage = request.getParameter("challengeMessage");
        if (challengeMessage == null) challengeMessage = "";

        if (challenged == null || challenged.isEmpty() || quizName == null || quizName.isEmpty()) {
            request.setAttribute("error", "Challenged user and quiz name are required.");
            request.getRequestDispatcher("profile?username=" + challenged).forward(request, response);
            return;
        }

        try {
            boolean friends = FriendDAO.areFriends(challenger, challenged);
            if (!friends) {
                request.setAttribute("error", "You can only challenge your friends.");
                request.getRequestDispatcher("profile?username=" + challenged).forward(request, response);
                return;
            }

            Quiz quiz = QuizDAO.getQuizByName(quizName);
            if (quiz == null) {
                request.setAttribute("error", "Quiz with name '" + quizName + "' not found.");
                request.getRequestDispatcher("profile?username=" + challenged).forward(request, response);
                return;
            }

            ChallengeDAO.addChallenge(
                    challenger,
                    challenged,
                    quiz.getId(),
                    challengeMessage,
                    0L,
                    0L,
                    null
            );

            response.sendRedirect("profile?username=" + challenged + "&challengeSent=1");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher("profile?username=" + challenged).forward(request, response);
        }
    }
}
