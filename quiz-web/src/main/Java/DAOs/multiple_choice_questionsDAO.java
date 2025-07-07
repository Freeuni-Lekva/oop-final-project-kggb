package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class multiple_choice_questionsDAO {
    public void addQuestion(MultipleChoiceQuestion q) throws SQLException {
        String sql = "INSERT INTO multiple_choice_questions (quiz_id, question, correct_answer, incorrect_choice_1, incorrect_choice_2, incorrect_choice_3, question_order, points) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, q.getQuizId());
            stmt.setString(2, q.getQuestion());
            stmt.setString(3, q.getCorrectAnswer());
            List<String> choices = q.getChoices();
            stmt.setString(4, choices.size() > 1 ? choices.get(1) : null);
            stmt.setString(5, choices.size() > 2 ? choices.get(2) : null);
            stmt.setString(6, choices.size() > 3 ? choices.get(3) : null);
            stmt.setInt(7, q.getQuestionOrder());
            stmt.setInt(8, q.getPoints());
            stmt.executeUpdate();
        }
    }

    public List<MultipleChoiceQuestion> getQuestionsByQuizId(long quizId) throws SQLException {
        List<MultipleChoiceQuestion> questions = new ArrayList<>();
        String sql = "SELECT * FROM multiple_choice_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                List<String> choices = new ArrayList<>();
                choices.add(rs.getString("correct_answer"));
                choices.add(rs.getString("incorrect_choice_1"));
                choices.add(rs.getString("incorrect_choice_2"));
                choices.add(rs.getString("incorrect_choice_3"));

                questions.add(new MultipleChoiceQuestion(
                        rs.getLong("id"),
                        quizId,
                        rs.getString("question"),
                        rs.getString("correct_answer"),
                        choices,
                        rs.getInt("question_order"),
                        rs.getInt("points")
                ));
            }
        }
        return questions;
    }

    public void deleteQuestion(long questionId) throws SQLException {
        String sql = "DELETE FROM multiple_choice_questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            stmt.executeUpdate();
        }
    }
}
