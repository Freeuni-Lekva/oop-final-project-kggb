import DAOs.PictureResponseQuestionDAO;
import DAOs.DBConnection;
import Models.PictureResponseQuestion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PictureResponseQuestionDAOTest {
    PictureResponseQuestionDAO dao = new PictureResponseQuestionDAO();

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM picture_response_questions")) {
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
                stmt.setString(2, "Geography Quiz");
                stmt.setString(3, "Geography");
                stmt.setString(4, "A quiz about world geography");
                stmt.setString(5, "ekali");
                stmt.setBoolean(6, false);
                stmt.setBoolean(7, false);
                stmt.setBoolean(8, false);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO picture_response_questions " +
                            "(id, quiz_id, picture_url, question, correct_answer, question_order, points) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setLong(2, 1L);
                stmt.setString(3, "https://example.com/flag.jpg");
                stmt.setString(4, "Which country's flag is this?");
                stmt.setString(5, "Canada");
                stmt.setInt(6, 1);
                stmt.setInt(7, 10);
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM picture_response_questions")) {
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
        PictureResponseQuestion q = new PictureResponseQuestion(
                0,
                1L,
                "https://example.com/monument.jpg",
                "What famous monument is this?",
                "Eiffel Tower",
                2,
                5
        );
        dao.addQuestion(q);
        List<PictureResponseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(2, questions.size());
        PictureResponseQuestion added = questions.stream()
                .filter(prq -> prq.getQuestion().equals("What famous monument is this?"))
                .findFirst()
                .orElse(null);
        assertNotNull(added);
        assertEquals("Eiffel Tower", added.getCorrectAnswer());
        assertEquals("https://example.com/monument.jpg", added.getPictureUrl());
        assertEquals(2, added.getQuestionOrder());
        assertEquals(5, added.getPoints());
    }

    @Test
    void testGet() throws Exception {
        List<PictureResponseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        PictureResponseQuestion q = questions.get(0);
        assertEquals("Which country's flag is this?", q.getQuestion());
        assertEquals("Canada", q.getCorrectAnswer());
        assertEquals("https://example.com/flag.jpg", q.getPictureUrl());
        assertEquals(1, q.getQuestionOrder());
        assertEquals(10, q.getPoints());
    }

    @Test
    void testDelete() throws Exception {
        List<PictureResponseQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        long idToDelete = questions.get(0).getId();
        dao.deleteQuestion(idToDelete);
        List<PictureResponseQuestion> afterDelete = dao.getQuestionsByQuizId(1L);
        assertTrue(afterDelete.isEmpty());
    }
}
