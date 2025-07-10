package DAOs;

import Models.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizDAO {

    public static Quiz getQuiz(long id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quizzes WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Quiz(id, rs.getString("quiz_name"), rs.getString("category"), rs.getString("description"),
                        rs.getString("creator"), rs.getString("date_created"), rs.getBoolean("randomized"),
                        rs.getBoolean("multi_page"), rs.getBoolean("immediate_score"));
            }
        }
        return null;
    }

    public static ArrayList<Quiz> getQuizzesByCreator(String creator, int limit) {
        ArrayList<Quiz> quizzes = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM quizzes WHERE creator = ? ORDER BY date_created DESC";
            if (limit > 0) {
                query += " LIMIT ?";
            }

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, creator);
            if (limit > 0) {
                ps.setInt(2, limit);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving quizzes by creator: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return quizzes;
    }

    public static int createQuiz(String name, String category, String description, String creator, String date_created,
                                 boolean randomized, boolean multi_page, boolean immediate_score) {
        String query = "SELECT max(id) as max_id FROM quizzes";
        int max_id = 1;
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("max_id") != null) {
                    max_id = rs.getInt("max_id") + 1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String date_formatted = dateFormat.format(date);

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO quizzes VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, max_id);
            ps.setString(2, name);
            ps.setString(3, category);
            ps.setString(4, description);
            ps.setString(5, creator);
            ps.setString(6, date_formatted);
            ps.setBoolean(7, randomized);
            ps.setBoolean(8, multi_page);
            ps.setBoolean(9, immediate_score);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return max_id;
    }

    public static void removeQuiz(int id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM quizzes WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM reported_quizzes WHERE quiz_id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public static ArrayList<Quiz> getQuizzes(int limit) {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM quizzes ORDER BY date_created DESC";
            if (limit > 0) {
                query += " LIMIT ?";
            }
            PreparedStatement ps = conn.prepareStatement(query);
            if (limit > 0) {
                ps.setInt(1, limit);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    public static ArrayList<Quiz> getQuizzesByCategory(String category) {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM quizzes WHERE category = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    public static ArrayList<Question> getQuestions(int quizId) throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        try {
            String tfQuery = "SELECT id, question, correct_answer, question_order, points FROM true_or_false_questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(tfQuery)) {
                stmt.setLong(1, quizId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    questions.add(new TrueFalseQuestion(rs.getLong("id"), quizId, rs.getString("question"),
                            rs.getBoolean("correct_answer"), rs.getInt("question_order"), rs.getInt("points")));
                }
            }

            String fibQuery = "SELECT id, question, correct_answer, question_order, points, case_sensitive FROM fill_in_the_blank_questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(fibQuery)) {
                stmt.setLong(1, quizId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    questions.add(new FillInTheBlankQuestion(rs.getLong("id"), quizId, rs.getString("question"),
                            rs.getString("correct_answer"), rs.getBoolean("case_sensitive"),
                            rs.getInt("question_order"), rs.getInt("points")));
                }
            }

            String mcQuery = "SELECT id, question, correct_answer, incorrect_choice_1, incorrect_choice_2, incorrect_choice_3, question_order, points FROM multiple_choice_questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(mcQuery)) {
                stmt.setLong(1, quizId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ArrayList<String> choices = new ArrayList<>();
                    choices.add(rs.getString("correct_answer"));
                    if (rs.getString("incorrect_choice_1") != null) choices.add(rs.getString("incorrect_choice_1"));
                    if (rs.getString("incorrect_choice_2") != null) choices.add(rs.getString("incorrect_choice_2"));
                    if (rs.getString("incorrect_choice_3") != null) choices.add(rs.getString("incorrect_choice_3"));

                    questions.add(new MultipleChoiceQuestion(rs.getLong("id"), quizId, rs.getString("question"),
                            rs.getString("correct_answer"), choices, rs.getInt("question_order"), rs.getInt("points")));
                }
            }

            String picQuery = "SELECT id, picture_url, question, correct_answer, question_order, points FROM picture_response_questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(picQuery)) {
                stmt.setLong(1, quizId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    questions.add(new PictureResponseQuestion(rs.getLong("id"), quizId, rs.getString("picture_url"),
                            rs.getString("question"), rs.getString("correct_answer"),
                            rs.getInt("question_order"), rs.getInt("points")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static ArrayList<Quiz> getPopularQuizzes(int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String query = "SELECT q.id AS quizId, q.quiz_name, q.category, q.description, q.creator, q.date_created, " +
                "q.randomized, q.multi_page, q.immediate_score FROM quizzes q INNER JOIN (" +
                "SELECT quiz_id, COUNT(*) AS count FROM quiz_takes_history GROUP BY quiz_id ORDER BY count DESC) AS popular " +
                "ON q.id = popular.quiz_id";

        if (limit > 0) {
            query += " LIMIT " + limit;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("quizId"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public static ArrayList<Quiz> getSimilarQuizName(String quizName, int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String query = "SELECT * FROM quizzes WHERE quiz_name LIKE ? ORDER BY date_created DESC";
        if (limit > 0) {
            query += " LIMIT " + limit;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + quizName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public static ArrayList<Quiz> getFriendsCreatedQuizzes(String username, int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String query = "SELECT q.id, q.quiz_name, q.category, q.description, q.creator, q.date_created, " +
                "q.randomized, q.multi_page, q.immediate_score FROM quizzes q INNER JOIN (" +
                "SELECT CASE WHEN first_friend_username = ? THEN second_friend_username " +
                "ELSE first_friend_username END AS friend FROM friends " +
                "WHERE first_friend_username = ? OR second_friend_username = ?) AS f ON q.creator = f.friend";

        if (limit > 0) {
            query += " LIMIT " + limit;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public static List<Quiz> getCreatedByUser(String username) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, quiz_name, category, description, creator, date_created " +
                     "FROM quizzes WHERE creator = ?")) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quizzes.add(new Quiz(
                            rs.getLong("id"),
                            rs.getString("quiz_name"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getString("creator"),
                            rs.getString("date_created"),
                            rs.getBoolean("randomized"),
                            rs.getBoolean("multipage"),
                            rs.getBoolean("immediate_score")
                    ));
                }
            }
        }

        return quizzes;
    }

    public static ArrayList<Quiz> getUnratedQuizzes(String username, int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String query = "SELECT DISTINCT q.id, q.quiz_name, q.category, q.description, q.creator, q.date_created, " +
                "q.randomized, q.multi_page, q.immediate_score FROM quiz_takes_history qth INNER JOIN quizzes q " +
                "ON qth.quiz_id = q.id LEFT JOIN quiz_ratings qr ON qth.quiz_id = qr.quiz_id AND qr.player = ? " +
                "WHERE qth.username = ? AND qr.quiz_id IS NULL";

        if (limit > 0) {
            query += " LIMIT " + limit;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizzes.add(new Quiz(rs.getInt("id"), rs.getString("quiz_name"), rs.getString("category"),
                        rs.getString("description"), rs.getString("creator"), rs.getString("date_created"),
                        rs.getBoolean("randomized"), rs.getBoolean("multi_page"), rs.getBoolean("immediate_score")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }
}
