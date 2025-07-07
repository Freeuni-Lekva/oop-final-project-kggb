package DAOs;

import Models.TrueFalseQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrueOrFalseQuestionDAO {
    public void addQuestion(TrueFalseQuestion q) throws SQLException {
        String sql = "INSERT INTO true_or_false_questions (quiz_id, question, correct_answer, question_order, points) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, q.getQuizId());
            stmt.setString(2, q.getQuestion());
            stmt.setBoolean(3, q.isCorrectAnswer());
            stmt.setInt(4, q.getQuestionOrder());
            stmt.setInt(5, q.getPoints());
            stmt.executeUpdate();
        }
    }
    public List<TrueFalseQuestion> getQuestionsByQuizId(long quizId) throws SQLException {
        List<TrueFalseQuestion> questions = new ArrayList<>();
        String sql = "SELECT * FROM true_or_false_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new TrueFalseQuestion(
                        rs.getLong("id"),
                        quizId,
                        rs.getString("question"),
                        rs.getBoolean("correct_answer"),
                        rs.getInt("question_order"),
                        rs.getInt("points")
                ));
            }
        }
        return questions;
    }

    public void deleteQuestion(long questionId) throws SQLException {
        String sql = "DELETE FROM true_or_false_questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            stmt.executeUpdate();
        }
    }
}
