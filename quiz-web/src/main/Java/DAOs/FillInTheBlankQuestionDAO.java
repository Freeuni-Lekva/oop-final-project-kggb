package DAOs;

import Models.FillInTheBlankQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FillInTheBlankQuestionDAO {
    public void addQuestion(FillInTheBlankQuestion q) throws SQLException {
        String sql = "INSERT INTO fill_in_the_blank_questions (quiz_id, question, correct_answer, question_order, points, case_sensitive) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, q.getQuizId());
            stmt.setString(2, q.getQuestion());
            stmt.setString(3, q.getCorrectAnswer());
            stmt.setInt(4, q.getQuestionOrder());
            stmt.setInt(5, q.getPoints());
            stmt.setBoolean(6, q.isCaseSensitive());
            stmt.executeUpdate();
        }
    }

    public List<FillInTheBlankQuestion> getQuestionsByQuizId(long quizId) throws SQLException {
        List<FillInTheBlankQuestion> questions = new ArrayList<>();
        String sql = "SELECT * FROM fill_in_the_blank_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new FillInTheBlankQuestion(
                        rs.getLong("id"),
                        quizId,
                        rs.getString("question"),
                        rs.getString("correct_answer"),
                        rs.getBoolean("case_sensitive"),
                        rs.getInt("question_order"),
                        rs.getInt("points")
                ));
            }
        }
        return questions;
    }

    public void deleteQuestion(long questionId) throws SQLException {
        String sql = "DELETE FROM fill_in_the_blank_questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            stmt.executeUpdate();
        }
    }
}
