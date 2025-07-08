import DAOs.MultipleChoiceQuestionDAO;
import DAOs.DBConnection;
import Models.MultipleChoiceQuestion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleChoiceQuestionDAOTest {
    MultipleChoiceQuestionDAO dao = new MultipleChoiceQuestionDAO();

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM multiple_choice_questions")) {
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
                stmt.setString(4, "ekali23");
                stmt.setString(5, null);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO quizzes (id, quiz_name, category, description, creator, randomized, multi_page, immediate_score) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setString(2, "Java Quiz");
                stmt.setString(3, "Programming");
                stmt.setString(4, "A quiz about Java programming");
                stmt.setString(5, "ekali");
                stmt.setBoolean(6, false);
                stmt.setBoolean(7, false);
                stmt.setBoolean(8, false);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO multiple_choice_questions " +
                            "(id, quiz_id, question, correct_answer, incorrect_choice_1, incorrect_choice_2, incorrect_choice_3, question_order, points) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setLong(1, 1L);
                stmt.setLong(2, 1L);
                stmt.setString(3, "What is the latest Java version?");
                stmt.setString(4, "Java 24");
                stmt.setString(5, "Java 8");
                stmt.setString(6, "Java 11");
                stmt.setString(7, "Java 14");
                stmt.setInt(8, 1);
                stmt.setInt(9, 10);
                stmt.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM multiple_choice_questions")) {
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
        MultipleChoiceQuestion q = new MultipleChoiceQuestion(
                0,
                1L,
                "Which keyword is used for inheritance in Java?",
                "extends",
                Arrays.asList("extends", "inherits", "implements", "derives"),
                2,
                5
        );
        dao.addQuestion(q);
        List<MultipleChoiceQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(2, questions.size());
        MultipleChoiceQuestion added = questions.stream()
                .filter(mcq -> mcq.getQuestion().equals("Which keyword is used for inheritance in Java?"))
                .findFirst()
                .orElse(null);

        assertNotNull(added);
        assertEquals("extends", added.getCorrectAnswer());
        assertEquals(4, added.getChoices().size());
        assertTrue(added.getChoices().contains("inherits"));
        assertEquals(2, added.getQuestionOrder());
        assertEquals(5, added.getPoints());
    }

    @Test
    void testGet() throws Exception {
        List<MultipleChoiceQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        MultipleChoiceQuestion q = questions.get(0);
        assertEquals("What is the latest Java version?", q.getQuestion());
        assertEquals("Java 24", q.getCorrectAnswer());
        assertEquals(4, q.getChoices().size());
        assertTrue(q.getChoices().contains("Java 8"));
        assertTrue(q.getChoices().contains("Java 11"));
        assertTrue(q.getChoices().contains("Java 14"));
        assertEquals(1, q.getQuestionOrder());
        assertEquals(10, q.getPoints());
    }

    @Test
    void testDelete() throws Exception {
        List<MultipleChoiceQuestion> questions = dao.getQuestionsByQuizId(1L);
        assertEquals(1, questions.size());
        long idToDelete = questions.get(0).getId();
        dao.deleteQuestion(idToDelete);
        List<MultipleChoiceQuestion> afterDelete = dao.getQuestionsByQuizId(1L);
        assertTrue(afterDelete.isEmpty());
    }
}
