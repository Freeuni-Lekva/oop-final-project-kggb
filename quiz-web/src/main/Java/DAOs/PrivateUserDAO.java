package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrivateUserDAO {
    private String username;

    public PrivateUserDAO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void addPrivateUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO private_users (username) VALUES (?)")){
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static void removePrivateUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM private_users WHERE username = ?")){
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean isPrivateUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM private_users WHERE username = ?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            return false;
        }
    }

    public static ArrayList<PrivateUserDAO> getAllPrivateUsers() throws SQLException {
        ArrayList<PrivateUserDAO> privateUsers = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM private_users")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                privateUsers.add(new PrivateUserDAO(resultSet.getString("username")));
            }
        }
        return privateUsers;
    }
}
