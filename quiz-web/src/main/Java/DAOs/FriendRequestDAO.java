package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendRequestDAO {

    private String requestFromUsername;
    private String requestToUsername;

    public FriendRequestDAO(String requestFromUsername, String requestToUsername) {
        this.requestFromUsername = requestFromUsername;
        this.requestToUsername = requestToUsername;
    }

    public String getRequestFromUsername() {
        return requestFromUsername;
    }

    public void setRequestFromUsername(String requestFromUsername) {
        this.requestFromUsername = requestFromUsername;
    }

    public String getRequestToUsername() {
        return requestToUsername;
    }

    public void setRequestToUsername(String requestToUsername) {
        this.requestToUsername = requestToUsername;
    }

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

    public static ArrayList<String> getFriendRequestsToUser(String username) throws SQLException {
        ArrayList<String> friendRequestsToUser = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friend_requests " +
                "WHERE request_sent_to = ?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                friendRequestsToUser.add(resultSet.getString("request_sent_from"));
            }
        }
        return friendRequestsToUser;
    }

    public static ArrayList<String> getFriendRequestsFromUser(String username) throws SQLException {
        ArrayList<String> friendRequestsFromUser = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friend_requests " +
                     "WHERE request_sent_from = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friendRequestsFromUser.add(resultSet.getString("request_sent_to"));
            }
        }
        return friendRequestsFromUser;
    }
}
