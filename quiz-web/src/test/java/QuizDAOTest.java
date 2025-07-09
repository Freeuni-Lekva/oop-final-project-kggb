import DAOs.DBConnection;
import DAOs.QuizDAO;
import DAOs.UserDAO;
import Models.Quiz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuizDAOTest {

    @BeforeEach
    void setup() throws Exception {
        try(Connection con = DBConnection.getConnection()){
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23', 'lalala', 'saba123')")) {
                stmt.executeUpdate();
            }

            String hashedPassword = UserDAO.hashPassword("1234"); // Get the SHA hash of "1234"
            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('tgela23', 'tamari', 'gelashvili', NOW(), ?, 'https://example.com/tamari.jpg')")) {
                stmt.setString(1, hashedPassword); // Use hashed password
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('ekali23', 'elene', 'kalinichenko', NOW(), ?, 'https://example.com/elene.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("5555")); // Hash "5555"
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('lbati23', 'luka', 'batilashvili', NOW(), ?, 'https://example.com/batila.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("6767")); // Hash "6767"
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('tgela23', 'ekali23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO admin_users (username) VALUES ('tgela23')")) {
                stmt.executeUpdate();
            }

            try(PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (01, 'literature_quiz', 'education', 'quiz about classic literature', 'tgela23', NOW(), false, true, true)"
            )){
                ps.execute();
            }
            try(PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (02, 'fashion_quiz', 'fashion', 'quiz about latest fashion trends', 'tgela23', NOW(), true, true, true)"
            )){
                ps.execute();
            }
            try(PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (03, '2000s music', 'music', 'find out how well you know your childhood music', 'lbati23', NOW(), true, true, false)"
            )){
                ps.execute();
            }
            try(PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (04, 'comedy movies', 'TV', 'take a rest and laugh with our quiz', 'ekali23', NOW(), false, true, true)"
            )){
                ps.execute();
            }
        }
    }

    @AfterEach
    void cleanUp() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM quizzes WHERE id IN (01, 02, 03, 04)")) {
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM friends WHERE first_friend_username IN ('tgela23', 'ekali23', 'lbati23') " +
                            "OR second_friend_username IN ('tgela23', 'ekali23', 'lbati23')")) {
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM admin_users WHERE username = 'tgela23'")) {
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23')")) {
                ps.executeUpdate();
            }
        }
    }

    @Test
    void testGetQuizById() throws SQLException {
        Quiz quiz = QuizDAO.getQuiz(1);
        Assertions.assertNotNull(quiz);
        Assertions.assertEquals("literature_quiz", quiz.getName());
    }

    @Test
    void testGetQuizzesByCreator() {
        ArrayList<Quiz> quizzes = QuizDAO.getQuizzesByCreator("tgela23", 10);
        Assertions.assertEquals(2, quizzes.size());
        Assertions.assertTrue(quizzes.stream().anyMatch(q -> q.getName().equals("literature_quiz")));
    }


    @Test
    void testRemoveQuiz() throws SQLException {
        int id = QuizDAO.createQuiz("temporary", "misc", "to be deleted", "tgela23", "2025-07-08", false, false, false);
        QuizDAO.removeQuiz(id);
        Assertions.assertNull(QuizDAO.getQuiz(id));
    }

    @Test
    void testGetQuizzes() {
        ArrayList<Quiz> quizzes = QuizDAO.getQuizzes(10);
        Assertions.assertTrue(quizzes.size() >= 4);
    }

    @Test
    void testGetQuizzesByCategory() {
        ArrayList<Quiz> quizzes = QuizDAO.getQuizzesByCategory("music");
        Assertions.assertEquals(1, quizzes.size());
        Assertions.assertEquals("2000s music", quizzes.get(0).getName());
    }

    @Test
    void testGetSimilarQuizName() throws SQLException {
        ArrayList<Quiz> quizzes = QuizDAO.getSimilarQuizName("music", 10);
        Assertions.assertFalse(quizzes.isEmpty());
        Assertions.assertTrue(quizzes.stream().anyMatch(q -> q.getName().contains("music")));
    }

    @Test
    void testGetFriendsCreatedQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes = QuizDAO.getFriendsCreatedQuizzes("tgela23", 10);
        Assertions.assertEquals(1, quizzes.size(), "Expected 1 quiz from direct friends of tgela23");
        Assertions.assertEquals("comedy movies", quizzes.get(0).getName());
    }


    @Test
    void testGetUnratedQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes = QuizDAO.getUnratedQuizzes("tgela23", 10);
        // Assuming tgela23 has taken quizzes but hasn't rated them.
        Assertions.assertNotNull(quizzes);
    }

    @Test
    void testGetPopularQuizzes() throws SQLException {
        ArrayList<Quiz> popular = QuizDAO.getPopularQuizzes(10);
        Assertions.assertNotNull(popular); // Will return empty unless quiz_takes_history is populated
    }

    @Test
    void testGetQuestionsReturnsEmpty() throws SQLException {
        ArrayList<?> questions = QuizDAO.getQuestions(1);
        Assertions.assertNotNull(questions); // Should be empty unless populated in setup
    }

}
