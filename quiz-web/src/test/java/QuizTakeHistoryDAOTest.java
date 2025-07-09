import DAOs.DBConnection;
import DAOs.QuizTakesHistoryDAO;
import DAOs.UserDAO;
import Models.QuizTakesHistory;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizTakeHistoryDAOTest {

    @BeforeEach
    public void setup() throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM quiz_takes_history")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM quizzes WHERE id IN (1, 2, 3, 4)")) {
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

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (1, 'literature_quiz', 'education', 'quiz about classic literature', 'tgela23', NOW(), false, true, true)")) {
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (2, 'fashion_quiz', 'fashion', 'quiz about latest fashion trends', 'tgela23', NOW(), true, true, true)")) {
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (3, '2000s music', 'music', 'find out how well you know your childhood music', 'lbati23', NOW(), true, true, false)")) {
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES (4, 'comedy movies', 'TV', 'take a rest and laugh with our quiz', 'ekali23', NOW(), false, true, true)")) {
                ps.execute();
            }
        }
    }

    @AfterEach
    public void cleanup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM quiz_takes_history")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM quizzes WHERE id IN (1,2,3,4)")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username IN ('tgela23', 'ekali23', 'lbati23')")) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    public void addAndGetByIdTest() throws SQLException {
        QuizTakesHistoryDAO dao = new QuizTakesHistoryDAO();
        Time timeSpent = Time.valueOf("00:05:30");
        QuizTakesHistory take = new QuizTakesHistory(0, "tgela23", 1, 8, 10, new Timestamp(System.currentTimeMillis()), timeSpent);
        dao.addQuizTake(take);

        List<QuizTakesHistory> historyList = dao.getAllTakesForUser("tgela23");
        assertFalse(historyList.isEmpty());

        QuizTakesHistory retrieved = dao.getById(historyList.get(0).getId());
        assertNotNull(retrieved);
        assertEquals("tgela23", retrieved.getUsername());
        assertEquals(8, retrieved.getScore());
        assertEquals(10, retrieved.getMaxScore());
        assertEquals(timeSpent, retrieved.getTimeSpent());
    }

    @Test
    public void getAllTakesForUserTest() throws SQLException {
        QuizTakesHistoryDAO dao = new QuizTakesHistoryDAO();

        dao.addQuizTake(new QuizTakesHistory(0, "ekali23", 1, 9, 10, new Timestamp(System.currentTimeMillis()), Time.valueOf("00:04:00")));
        dao.addQuizTake(new QuizTakesHistory(0, "ekali23", 2, 7, 10, new Timestamp(System.currentTimeMillis()), Time.valueOf("00:06:00")));

        List<QuizTakesHistory> history = dao.getAllTakesForUser("ekali23");
        assertEquals(2, history.size());
        assertEquals("ekali23", history.get(0).getUsername());
    }

    @Test
    public void deleteByIdTest() throws SQLException {
        QuizTakesHistoryDAO dao = new QuizTakesHistoryDAO();

        QuizTakesHistory take = new QuizTakesHistory(0, "lbati23", 3, 6, 10, new Timestamp(System.currentTimeMillis()), Time.valueOf("00:03:00"));
        dao.addQuizTake(take);
        List<QuizTakesHistory> all = dao.getAllTakesForUser("lbati23");

        assertFalse(all.isEmpty());
        long idToDelete = all.get(0).getId();

        dao.deleteById(idToDelete);
        QuizTakesHistory deleted = dao.getById(idToDelete);
        assertNull(deleted);
    }
}
