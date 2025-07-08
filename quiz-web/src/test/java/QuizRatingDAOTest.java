import DAOs.DBConnection;
import DAOs.QuizRatingDAO;
import DAOs.UserDAO;
import Models.QuizRating;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizRatingDAOTest {

    @BeforeEach
    public void setup() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23', 'lalala', 'saba123')")) {
                stmt.executeUpdate();
            }

            String hashedPassword = UserDAO.hashPassword("1234");
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('tgela23', 'tamari', 'gelashvili', NOW(), ?, 'https://example.com/tamari.jpg')")) {
                stmt.setString(1, hashedPassword);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('ekali23', 'elene', 'kalinichenko', NOW(), ?, 'https://example.com/elene.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("5555"));
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES ('lbati23', 'luka', 'batilashvili', NOW(), ?, 'https://example.com/batila.jpg')")) {
                stmt.setString(1, UserDAO.hashPassword("6767"));
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('tgela23', 'ekali23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO friends (first_friend_username, second_friend_username) VALUES ('ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO admin_users (username) VALUES ('tgela23')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (01, 'literature_quiz', 'education', 'quiz about classic literature', 'tgela23', NOW(), false, true, true)")) {
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (02, 'fashion_quiz', 'fashion', 'quiz about latest fashion trends', 'tgela23', NOW(), true, true, true)")) {
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (03, '2000s music', 'music', 'find out how well you know your childhood music', 'lbati23', NOW(), true, true, false)")) {
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (04, 'comedy movies', 'TV', 'take a rest and laugh with our quiz', 'ekali23', NOW(), false, true, true)")) {
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM quiz_ratings WHERE id in (1, 2, 3, 4)")) {
                ps.execute();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO quiz_ratings (id, player, quiz_id, rating, review, created_at) " +
                            "VALUES (1, 'ekali23', 01, 5, 'interesting', NOW())")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO quiz_ratings (id, player, quiz_id, rating, review, created_at) " +
                            "VALUES (2, 'lbati23', 02, 4, 'fun', NOW())")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO quiz_ratings (id, player, quiz_id, rating, review, created_at) " +
                            "VALUES (3, 'tgela23', 03, 5, 'relaxing', NOW())")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO quiz_ratings (id, player, quiz_id, rating, review, created_at) " +
                            "VALUES (4, 'lbati23', 04, 3, 'amusing', NOW())")) {
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM quizzes WHERE id in (01, 02, 03, 04)")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM quiz_ratings WHERE id in (1, 2, 3, 4, 5, 6)")) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    void addQuizRatingTest() throws SQLException {
        QuizRating quizRating = new QuizRating(
                5,
                "tgela23",
                4,
                5,
                "very good",
                new Timestamp(System.currentTimeMillis())
        );
        QuizRatingDAO ratingDAO = new QuizRatingDAO();
        ratingDAO.addQuizRating(quizRating);

        QuizRating retrieved = ratingDAO.getQuizRating("tgela23", 4);
        assertNotNull(retrieved);
        assertEquals("tgela23", retrieved.getPlayer());
        assertEquals(4, retrieved.getQuizId());
        assertEquals(5, retrieved.getRating());
        assertEquals("very good", retrieved.getReview());
    }

    @Test
    void getQuizRatingTest() throws SQLException {
        QuizRatingDAO dao = new QuizRatingDAO();
        QuizRating rating = dao.getQuizRating("ekali23", 1);
        assertNotNull(rating);
        assertEquals("interesting", rating.getReview());
        assertEquals(5, rating.getRating());
    }

    @Test
    void updateQuizRatingTest() throws SQLException {
        QuizRatingDAO dao = new QuizRatingDAO();
        QuizRating existing = dao.getQuizRating("lbati23", 2);
        assertNotNull(existing);

        QuizRating updated = new QuizRating(existing.getId(), "lbati23", 2, 5, "updated review", existing.getCreatedAt());
        dao.updateQuizRating(updated);

        QuizRating fetched = dao.getQuizRating("lbati23", 2);
        assertNotNull(fetched);
        assertEquals(5, fetched.getRating());
        assertEquals("updated review", fetched.getReview());
    }

    @Test
    void deleteQuizRatingTest() throws SQLException {
        QuizRatingDAO dao = new QuizRatingDAO();
        QuizRating toDelete = new QuizRating(6, "tgela23", 1, 5, "temp", new Timestamp(System.currentTimeMillis()));
        dao.addQuizRating(toDelete);

        assertNotNull(dao.getQuizRating("tgela23", 1));

        dao.deleteQuizRating("tgela23", 1);
        QuizRating deleted = dao.getQuizRating("tgela23", 1);
        assertNull(deleted);
    }

    @Test
    void getRatingsForQuizTest() throws SQLException {
        QuizRatingDAO dao = new QuizRatingDAO();
        List<QuizRating> ratings = dao.getRatingsForQuiz(2);

        assertEquals(1, ratings.size());
        QuizRating r = ratings.get(0);
        assertEquals("lbati23", r.getPlayer());
        assertEquals(4, r.getRating());
        assertEquals("fun", r.getReview());
    }
}
