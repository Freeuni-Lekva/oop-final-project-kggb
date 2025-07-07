package DAOs;

import Models.Friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendDAO {

    public static void addFriends(String firstFriendUsername, String secondFriendUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("Insert into friends " +
                "(first_friend_username, second_friend_username) VALUES (?, ?)")){
            preparedStatement.setString(1, firstFriendUsername);
            preparedStatement.setString(2, secondFriendUsername);
            preparedStatement.executeUpdate();
        }
    }

    public void removeFriends(String firstFriendUsername, String secondFriendUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM friends " +
                "WHERE (first_friend_username = ? AND second_friend_username = ?) " +
                "OR (first_friend_username = ? AND second_friend_username = ?)")){
            preparedStatement.setString(1, firstFriendUsername);
            preparedStatement.setString(2, secondFriendUsername);
            preparedStatement.setString(3, secondFriendUsername);
            preparedStatement.setString(4, firstFriendUsername);
            preparedStatement.executeUpdate();
        }
    }

    public boolean areFriends(String firstFriendUsername, String secondFriendUsername) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friends " +
                "WHERE (first_friend_username = ? AND second_friend_username = ?) " +
                "OR (first_friend_username = ? AND second_friend_username = ?)") ){
            preparedStatement.setString(1, firstFriendUsername);
            preparedStatement.setString(2, secondFriendUsername);
            preparedStatement.setString(3, secondFriendUsername);
            preparedStatement.setString(4, firstFriendUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            return false;
        }
    }

    public ArrayList<Friend> getFriendsOfUser(String username) throws SQLException {
        ArrayList<Friend> friends = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("Select * FROM friends " +
                "Where first_friend_username = ? OR second_friend_username = ?")){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String firstFriend = resultSet.getString("first_friend_username");
                String secondFriend = resultSet.getString("second_friend_username");
                friends.add(new Friend(firstFriend, secondFriend));
            }
        }
        return friends;
    }
}
