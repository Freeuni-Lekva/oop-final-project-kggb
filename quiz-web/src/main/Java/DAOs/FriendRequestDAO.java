package DAOs;

import Models.FriendRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendRequestDAO {

    public static void addFriendRequest(String requestFromUsername, String requestToUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO friend_requests " +
                "(request_sent_from, request_sent_to) VALUES (?, ?)")){
            preparedStatement.setString(1, requestFromUsername);
            preparedStatement.setString(2, requestToUsername);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeFriendRequest(String requestFromUsername, String requestToUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM friend_requests " +
                "WHERE (request_sent_from = ? AND request_sent_to = ?)")){
            preparedStatement.setString(1, requestFromUsername);
            preparedStatement.setString(2, requestToUsername);
            preparedStatement.executeUpdate();
        }

    }

    public static boolean friendRequestExists(String requestFromUsername, String requestToUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friend_requests " +
                "WHERE (request_sent_from = ? AND request_sent_to = ?)")){
            preparedStatement.setString(1, requestFromUsername);
            preparedStatement.setString(2, requestToUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            return false;
        }
    }

    public static ArrayList<FriendRequest> getFriendRequestsToUser(String username) throws SQLException {
        ArrayList<FriendRequest> friendRequestsToUser = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friend_requests " +
                "WHERE request_sent_to = ?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                friendRequestsToUser.add(new FriendRequest(
                        resultSet.getString("request_sent_from"),
                        resultSet.getString("request_sent_to")
                ));
            }
        }
        return friendRequestsToUser;
    }

    public static ArrayList<FriendRequest> getFriendRequestsFromUser(String username) throws SQLException {
        ArrayList<FriendRequest> friendRequestsFromUser = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friend_requests " +
                     "WHERE request_sent_from = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friendRequestsFromUser.add(new FriendRequest(
                        resultSet.getString("request_sent_from"),
                        resultSet.getString("request_sent_to")
                ));
            }
        }
        return friendRequestsFromUser;
    }
}
