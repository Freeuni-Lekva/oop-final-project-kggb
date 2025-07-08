import DAOs.TrueOrFalseQuestionDAO;
import DAOs.DBConnection;
import Models.TrueFalseQuestion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrueOrFalseQuestionDAOTest {
    TrueOrFalseQuestionDAO dao = new TrueOrFalseQuestionDAO();

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM true_or_false_questions")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM quizzes")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password, profile_picture) " +
                            "VALUES (?, ?, ?, NOW(), ?, ?)")) {
                stmt.setString(1, "ekali");
                stmt.setString(2, "elene");
                stmt.setString(3, "kalinichenko");
                stmt.setString(4, "encrypted123");
                stmt.setString(5, null);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, randomized, multi_page, immediate_score) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setString(2, "Science Quiz");
                stmt.setString(3, "Science");
                stmt.setString(4, "A quiz about basic science");
                stmt.setString(5, "ekali");
                stmt.setBoolean(6, false);
                stmt.setBoolean(7, false);
                stmt.setBoolean(8, false);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO true_or_false_questions " +
                            "(id, quiz_id, question, correct_answer, question_order, points) " +
                            "VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setLong(2, 1L);
                stmt.setString(3, "The Earth is flat.");
                stmt.setBoolean(4, false);
                stmt.setInt(5, 1);
                stmt.setInt(6, 5);
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM true_or_false_questions")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM quizzes")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users")) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    void testAdd() throws Exception {
        TrueFalseQuestion q = new TrueFalseQuestion(
                0,
                1L,
                "Water boils at 100°C at sea level.",
                true,
                2,
                5
        );

        dao.addQuestion(q);
        List<TrueFalseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(2, questions.size());
        TrueFalseQuestion added = questions.stream()
                .filter(tfq -> tfq.getQuestion().equals("Water boils at 100°C at sea level."))
                .findFirst()
                .orElse(null);
        assertNotNull(added);
        assertTrue(added.isCorrectAnswer());
        assertEquals(2, added.getQuestionOrder());
        assertEquals(5, added.getPoints());
    }

    @Test
    void testGet() throws Exception {
        List<TrueFalseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        TrueFalseQuestion q = questions.get(0);
        assertEquals("The Earth is flat.", q.getQuestion());
        assertFalse(q.isCorrectAnswer());
        assertEquals(1, q.getQuestionOrder());
        assertEquals(5, q.getPoints());
    }

    @Test
    void testDelete() throws Exception {
        List<TrueFalseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        long idToDelete = questions.get(0).getId();
        dao.deleteQuestion(idToDelete);
        List<TrueFalseQuestion> afterDelete = dao.getQuestionsByQuizId(1L);
        assertTrue(afterDelete.isEmpty());
    }
}
