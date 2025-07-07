package DAOs;


import Models.Message;

import java.sql.*;
import java.util.ArrayList;


public class MessageDAO {

    public static void addMessage( String sentFrom, String sentTo, String message, String title) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages " +
                "(sent_from, sent_to, message, title) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, sentFrom);
            preparedStatement.setString(2, sentTo);
            preparedStatement.setString(3, message);
            preparedStatement.setString(4, title);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeMessage(long id) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public static ArrayList<Message> messagesSentToUser(String username) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE sent_to = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                messages.add(new Message(
                        resultSet.getLong("id"),
                        resultSet.getString("sent_from"),
                        resultSet.getString("sent_to"),
                        resultSet.getString("message"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("sent_at")
                ));
            }
        }
        return messages;
    }

    public static ArrayList<Message> messagesSentFromUser(String username) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE sent_from = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                messages.add(new Message(
                        resultSet.getLong("id"),
                        resultSet.getString("sent_from"),
                        resultSet.getString("sent_to"),
                        resultSet.getString("message"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("sent_at")
                ));
            }
        }
        return messages;
    }


}
