package DAOs;

import Models.Challenge;

import java.sql.*;
import java.util.ArrayList;

public class ChallengeDAO {
    public static void addChallenge(String challenger, String challenged, long quizID, String challengeMessage,
                                    long challengerScore, long challengedScore, Timestamp completedAt) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO challenges " +
                "(challenger, challenged, quiz_id, challenge_message, challenger_score, challenged_score, completed_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)" )) {
            preparedStatement.setString(1, challenger);
            preparedStatement.setString(2, challenged);
            preparedStatement.setLong(3, quizID);
            preparedStatement.setString(4, challengeMessage);
            preparedStatement.setLong(5, challengerScore);
            preparedStatement.setLong(6, challengedScore);
            preparedStatement.setTimestamp(7, completedAt);
            preparedStatement.executeUpdate();
        }

    }

    public static void removeChallenge(long id) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM challenges WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }

    }

    public static void changeChallengeStatus(long id, String status) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE challenges SET status = ? WHERE id = ?")) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        }

    }

    public static ArrayList<Challenge> getChallengesSentToUser(String username) throws SQLException {
        ArrayList<Challenge> challenges = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM challenges WHERE challenged = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                challenges.add(new Challenge(
                        resultSet.getLong("id"),
                        resultSet.getString("challenger"),
                        resultSet.getString("challenged"),
                        resultSet.getLong("quiz_id"),
                        resultSet.getString("challenge_message"),
                        resultSet.getTimestamp("sent_at"),
                        resultSet.getString("status"),
                        resultSet.getLong("challenger_score"),
                        resultSet.getLong("challenged_score"),
                        resultSet.getTimestamp("completed_at")
                ));
            }
        }
        return challenges;
    }

    public static ArrayList<Challenge> getAllChallenges() throws SQLException {
        ArrayList<Challenge> challenges = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM challenges")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                challenges.add(new Challenge(
                        resultSet.getLong("id"),
                        resultSet.getString("challenger"),
                        resultSet.getString("challenged"),
                        resultSet.getLong("quiz_id"),
                        resultSet.getString("challenge_message"),
                        resultSet.getTimestamp("sent_at"),
                        resultSet.getString("status"),
                        resultSet.getLong("challenger_score"),
                        resultSet.getLong("challenged_score"),
                        resultSet.getTimestamp("completed_at")
                ));
            }
        }
        return challenges;
    }
}
