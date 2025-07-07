package DAOs;

import Models.QuizRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizRatingDAO {
    public void addQuizRating(QuizRating rating) throws SQLException {
        String sql = "INSERT INTO quiz_ratings (player, quiz_id, rating, review) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rating.getPlayer());
            stmt.setLong(2, rating.getQuizId());
            stmt.setInt(3, rating.getRating());
            stmt.setString(4, rating.getReview());

            stmt.executeUpdate();
        }
    }

    public QuizRating getQuizRating(String player, long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_ratings WHERE player = ? AND quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player);
            stmt.setLong(2, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new QuizRating(
                            rs.getLong("id"),
                            rs.getString("player"),
                            rs.getLong("quiz_id"),
                            rs.getInt("rating"),
                            rs.getString("review"),
                            rs.getTimestamp("created_at")
                    );
                }
                return null;
            }
        }
    }

    public void updateQuizRating(QuizRating rating) throws SQLException {
        String sql = "UPDATE quiz_ratings SET rating = ?, review = ? WHERE player = ? AND quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rating.getRating());
            stmt.setString(2, rating.getReview());
            stmt.setString(3, rating.getPlayer());
            stmt.setLong(4, rating.getQuizId());

            stmt.executeUpdate();
        }
    }

    public void deleteQuizRating(String player, long quizId) throws SQLException {
        String sql = "DELETE FROM quiz_ratings WHERE player = ? AND quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player);
            stmt.setLong(2, quizId);

            stmt.executeUpdate();
        }
    }

    public List<QuizRating> getRatingsForQuiz(long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_ratings WHERE quiz_id = ?";
        List<QuizRating> ratings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ratings.add(new QuizRating(
                            rs.getLong("id"),
                            rs.getString("player"),
                            rs.getLong("quiz_id"),
                            rs.getInt("rating"),
                            rs.getString("review"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }

        return ratings;
    }
}
