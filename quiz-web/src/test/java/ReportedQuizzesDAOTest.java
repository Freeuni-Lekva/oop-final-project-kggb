import DAOs.DBConnection;
import DAOs.ReportedQuizzesDAO; // Corrected typo
import Models.ReportedQuiz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportedQuizzesDAOTest {
    private final ReportedQuizzesDAO dao = new ReportedQuizzesDAO(); // Corrected typo
    private long quizId; // Store the generated quiz ID

    @BeforeEach
    void setup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM reported_quizzes WHERE username IN ('testuser1', 'testuser2')")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM quizzes WHERE quiz_name = 'Test Quiz'")) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('testuser1', 'testuser2')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, first_name, last_name, date_joined, encrypted_password) " +
                            "VALUES ('testuser1', 'Test', 'User1', NOW(), 'dummypass'), " +
                            "('testuser2', 'Test', 'User2', NOW(), 'dummypass')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO quizzes (quiz_name, category, description, creator, date_created, randomized, multi_page, immediate_score) " +
                            "VALUES ('Test Quiz', 'general', 'A test quiz', 'testuser1', NOW(), false, false, false)",
                    Statement.RETURN_GENERATED_KEYS)) {
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    quizId = rs.getLong(1);
                } else {
                    throw new SQLException("Failed to retrieve generated quiz ID");
                }
            }
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM reported_quizzes WHERE username IN ('testuser1', 'testuser2')")) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM quizzes WHERE id = ?")) {
                stmt.setLong(1, quizId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('testuser1', 'testuser2')")) {
                stmt.executeUpdate();
            }
        }
    }

    @Test
    void addAndGetReportTest() throws Exception {
        ReportedQuiz report = new ReportedQuiz(
                quizId,
                "testuser1",
                "This quiz contains errors.",
                "content",
                new Timestamp(System.currentTimeMillis()),
                "pending"
        );

        dao.addReport(report);

        ReportedQuiz fetched = dao.getReport(quizId, "testuser1");
        assertNotNull(fetched, "Fetched report should not be null");
        assertEquals(report.getQuizId(), fetched.getQuizId(), "Quiz ID should match");
        assertEquals(report.getUsername(), fetched.getUsername(), "Username should match");
        assertEquals(report.getMessage(), fetched.getMessage(), "Message should match");
        assertEquals(report.getReportType(), fetched.getReportType(), "Report type should match");
        assertEquals(report.getStatus(), fetched.getStatus(), "Status should match");
        assertNotNull(fetched.getReportedAt(), "Reported at should not be null");
    }

    @Test
    void updateStatusTest() throws Exception {
        ReportedQuiz report = new ReportedQuiz(
                quizId,
                "testuser2",
                "Incorrect answers.",
                "content",
                new Timestamp(System.currentTimeMillis()),
                "pending"
        );

        dao.addReport(report);

        dao.updateStatus(quizId, "testuser2", "reviewed");

        ReportedQuiz updated = dao.getReport(quizId, "testuser2");
        assertNotNull(updated, "Updated report should not be null");
        assertEquals("reviewed", updated.getStatus(), "Status should be updated to reviewed");
    }

    @Test
    void deleteReportTest() throws Exception {
        ReportedQuiz report = new ReportedQuiz(
                quizId,
                "testuser1",
                "Typo in question.",
                "format",
                new Timestamp(System.currentTimeMillis()),
                "pending"
        );

        dao.addReport(report);

        // Confirm report exists
        assertNotNull(dao.getReport(quizId, "testuser1"), "Report should exist before deletion");

        // Delete it
        dao.deleteReport(quizId, "testuser1");

        // Confirm deletion
        assertNull(dao.getReport(quizId, "testuser1"), "Report should be deleted");
    }

    @Test
    void getAllReportsTest() throws Exception {
        // Clean all to start fresh
        cleanup();
        setup(); // Re-run setup to ensure quiz exists

        // Insert multiple reports
        ReportedQuiz report1 = new ReportedQuiz(
                quizId,
                "testuser1",
                "Issue 1",
                "content",
                new Timestamp(System.currentTimeMillis()),
                "pending"
        );
        ReportedQuiz report2 = new ReportedQuiz(
                quizId,
                "testuser2",
                "Issue 2",
                "format",
                new Timestamp(System.currentTimeMillis()),
                "pending"
        );

        dao.addReport(report1);
        dao.addReport(report2);

        List<ReportedQuiz> reports = dao.getAllReports();
        assertTrue(reports.stream().anyMatch(r -> r.getUsername().equals("testuser1")), "Report for testuser1 should exist");
        assertTrue(reports.stream().anyMatch(r -> r.getUsername().equals("testuser2")), "Report for testuser2 should exist");
    }
}