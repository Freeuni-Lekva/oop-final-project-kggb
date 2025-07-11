package DAOs;

import Models.QuizTakesHistory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizTakesHistoryDAO {
    public void addQuizTake(QuizTakesHistory take) throws SQLException {
        String sql = "INSERT INTO quiz_takes_history (username, quiz_id, score, max_score, time_spent) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, take.getUsername());
            stmt.setLong(2, take.getQuizId());
            stmt.setLong(3, take.getScore());
            stmt.setLong(4, take.getMaxScore());
            stmt.setTime(5, take.getTimeSpent());

            stmt.executeUpdate();
        }
    }

    public QuizTakesHistory getById(long id) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    );
                }
                return null;
            }
        }
    }

    public static List<QuizTakesHistory> getAllTakesForUser(String username) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE username = ? ORDER BY time_taken DESC";
        List<QuizTakesHistory> historyList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historyList.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return historyList;
    }

    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM quiz_takes_history WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public static List<QuizTakesHistory> getRecentTakesByFriends(String username) throws SQLException {
        String sql = " SELECT qth.*\n" +
                "        FROM quiz_takes_history qth\n" +
                "        JOIN (\n" +
                "            SELECT second_friend_username AS friend FROM friends WHERE first_friend_username = ?\n" +
                "            UNION\n" +
                "            SELECT first_friend_username AS friend FROM friends WHERE second_friend_username = ?\n" +
                "        ) f ON qth.username = f.friend\n" +
                "        ORDER BY qth.time_taken DESC";


        List<QuizTakesHistory> friendTakes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    friendTakes.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return friendTakes;
    }

    public static List<QuizTakesHistory> getUserAttemptsOnQuiz(String username, long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE username = ? AND quiz_id = ? ORDER BY time_taken DESC";
        List<QuizTakesHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setLong(2, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return list;
    }
    public static List<QuizTakesHistory> getTopPerformersAllTime(long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE quiz_id = ? ORDER BY (score * 1.0 / max_score) DESC LIMIT 5";
        List<QuizTakesHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return list;
    }

    public static List<QuizTakesHistory> getTopPerformersToday(long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE quiz_id = ? AND time_taken >= NOW() - INTERVAL 1 DAY ORDER BY (score * 1.0 / max_score) DESC LIMIT 5";
        List<QuizTakesHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return list;
    }

    public static List<QuizTakesHistory> getRecentAttempts(long quizId) throws SQLException {
        String sql = "SELECT * FROM quiz_takes_history WHERE quiz_id = ? ORDER BY time_taken DESC LIMIT 10";
        List<QuizTakesHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new QuizTakesHistory(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getLong("quiz_id"),
                            rs.getLong("score"),
                            rs.getLong("max_score"),
                            rs.getTimestamp("time_taken"),
                            rs.getTime("time_spent")
                    ));
                }
            }
        }

        return list;
    }

    public static double getAverageScore(long quizId) throws SQLException {
        String sql = "SELECT AVG(score * 1.0 / max_score) AS avg_score FROM quiz_takes_history WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("avg_score") * 100;
            }
        }
        return 0.0;
    }

    public static double getMaxScore(long quizId) throws SQLException {
        String sql = "SELECT MAX(score * 1.0 / max_score) AS max_score FROM quiz_takes_history WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("max_score") * 100;
            }
        }
        return 0.0;
    }

    public static double getMinScore(long quizId) throws SQLException {
        String sql = "SELECT MIN(score * 1.0 / max_score) AS min_score FROM quiz_takes_history WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("min_score") * 100;
            }
        }
        return 0.0;
    }

    public static int getTotalAttempts(long quizId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM quiz_takes_history WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        }
        return 0;
    }

}
