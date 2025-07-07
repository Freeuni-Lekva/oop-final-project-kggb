package DAOs;

import Models.ReportedQuiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class ReportedQuizzeDAO {
    public void addReport(ReportedQuiz report) throws SQLException {
        String sql = "INSERT INTO reported_quizzes (quiz_id, username, message, report_type, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, report.getQuizId());
            stmt.setString(2, report.getUsername());
            stmt.setString(3, report.getMessage());
            stmt.setString(4, report.getReportType());
            stmt.setString(5, report.getStatus());

            stmt.executeUpdate();
        }
    }

    public ReportedQuiz getReport(long quizId, String username) throws SQLException {
        String sql = "SELECT * FROM reported_quizzes WHERE quiz_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ReportedQuiz(
                            rs.getLong("quiz_id"),
                            rs.getString("username"),
                            rs.getString("message"),
                            rs.getString("report_type"),
                            rs.getTimestamp("reported_at"),
                            rs.getString("status")
                    );
                }
                return null;
            }
        }
    }

    public void updateStatus(long quizId, String username, String newStatus) throws SQLException {
        String sql = "UPDATE reported_quizzes SET status = ? WHERE quiz_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setLong(2, quizId);
            stmt.setString(3, username);

            stmt.executeUpdate();
        }
    }

    public void deleteReport(long quizId, String username) throws SQLException {
        String sql = "DELETE FROM reported_quizzes WHERE quiz_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            stmt.setString(2, username);

            stmt.executeUpdate();
        }
    }

    public List<ReportedQuiz> getAllReports() throws SQLException {
        String sql = "SELECT * FROM reported_quizzes ORDER BY reported_at DESC";
        List<ReportedQuiz> reports = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reports.add(new ReportedQuiz(
                        rs.getLong("quiz_id"),
                        rs.getString("username"),
                        rs.getString("message"),
                        rs.getString("report_type"),
                        rs.getTimestamp("reported_at"),
                        rs.getString("status")
                ));
            }
        }

        return reports;
    }
}
