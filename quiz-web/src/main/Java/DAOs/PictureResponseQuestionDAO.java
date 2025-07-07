package DAOs;
import Models.PictureResponseQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PictureResponseQuestionDAO {
    public void addQuestion(PictureResponseQuestion q) throws SQLException {
        String sql = "INSERT INTO picture_response_questions (quiz_id, picture_url, question, correct_answer, question_order, points) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, q.getQuizId());
            stmt.setString(2, q.getPictureUrl());
            stmt.setString(3, q.getQuestion());
            stmt.setString(4, q.getCorrectAnswer());
            stmt.setInt(5, q.getQuestionOrder());
            stmt.setInt(6, q.getPoints());
            stmt.executeUpdate();
        }
    }

    public List<PictureResponseQuestion> getQuestionsByQuizId(long quizId) throws SQLException {
        List<PictureResponseQuestion> questions = new ArrayList<>();
        String sql = "SELECT * FROM picture_response_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new PictureResponseQuestion(
                        rs.getLong("id"),
                        quizId,
                        rs.getString("picture_url"),
                        rs.getString("question"),
                        rs.getString("correct_answer"),
                        rs.getInt("question_order"),
                        rs.getInt("points")
                ));
            }
        }
        return questions;
    }

    public void deleteQuestion(long questionId) throws SQLException {
        String sql = "DELETE FROM picture_response_questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            stmt.executeUpdate();
        }
    }
}
