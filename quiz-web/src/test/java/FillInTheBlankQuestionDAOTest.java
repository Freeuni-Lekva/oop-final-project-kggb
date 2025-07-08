import DAOs.FillInTheBlankQuestionDAO;
import DAOs.DBConnection;
import Models.FillInTheBlankQuestion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FillInTheBlankQuestionDAOTest {
    FillInTheBlankQuestionDAO dao = new FillInTheBlankQuestionDAO();

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM fill_in_the_blank_questions")) {
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
                stmt.setString(1, "elene");
                stmt.setString(2, "Elene");
                stmt.setString(3, "Kalinichenko");
                stmt.setString(4, "encrypted123"); // dummy encrypted password
                stmt.setString(5, null); // profile_picture can be null
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, randomized, multi_page, immediate_score) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setString(2, "Sample Quiz");
                stmt.setString(3, "Programming");
                stmt.setString(4, "A test quiz about Java.");
                stmt.setString(5, "elene");
                stmt.setBoolean(6, false);
                stmt.setBoolean(7, false);
                stmt.setBoolean(8, false);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO fill_in_the_blank_questions " +
                            "(quiz_id, question, correct_answer, question_order, points, case_sensitive) " +
                            "VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setString(2, "Java is a ____ language.");
                stmt.setString(3, "programming");
                stmt.setInt(4, 1);
                stmt.setInt(5, 5);
                stmt.setBoolean(6, false);
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM fill_in_the_blank_questions")) {
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
        FillInTheBlankQuestion q = new FillInTheBlankQuestion(
                0,
                1L,
                "This is a test ____.",
                "question",
                true,
                2,
                4
        );
        dao.addQuestion(q);
        List<FillInTheBlankQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(2, questions.size()); // 1 from setup + 1 from this test
        FillInTheBlankQuestion added = questions.stream()
                .filter(f -> f.getQuestion().equals("This is a test ____."))
                .findFirst()
                .orElse(null);
        assertNotNull(added);
        assertEquals("question", added.getCorrectAnswer());
        assertTrue(added.isCaseSensitive());
        assertEquals(2, added.getQuestionOrder());
        assertEquals(4, added.getPoints());
    }

    @Test
    void testGet() throws Exception {
        List<FillInTheBlankQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        FillInTheBlankQuestion q = questions.get(0);
        assertEquals("Java is a ____ language.", q.getQuestion());
        assertEquals("programming", q.getCorrectAnswer());
        assertFalse(q.isCaseSensitive());
        assertEquals(1, q.getQuestionOrder());
        assertEquals(5, q.getPoints());
    }

    @Test
    void testDelete() throws Exception {
        List<FillInTheBlankQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        long idToDelete = questions.get(0).getId();
        dao.deleteQuestion(idToDelete);
        List<FillInTheBlankQuestion> afterDelete = dao.getQuestionsByQuizId(1L);
        assertTrue(afterDelete.isEmpty());
    }
}
