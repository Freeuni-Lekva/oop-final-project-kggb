package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminUserDAO {

    private String username;

    public AdminUserDAO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void addAdminUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO admin_users (username) VALUES (?)")){
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeAdminUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM admin_users WHERE username = ?")){
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }

    }

    public static boolean isAdminUser(String username) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM admin_users WHERE username = ?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            return false;
        }

    }

    public static ArrayList<AdminUserDAO> getAllAdminUsers() throws SQLException {
        ArrayList<AdminUserDAO> adminUsers = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM admin_users")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                adminUsers.add(new AdminUserDAO(resultSet.getString("username")));
            }
        }
        return adminUsers;
    }
}


