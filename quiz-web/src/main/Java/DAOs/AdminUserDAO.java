package DAOs;

import Models.AdminUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminUserDAO {

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

    public static ArrayList<AdminUser> getAllAdminUsers() throws SQLException {
        ArrayList<AdminUser> adminUsers = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM admin_users")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                adminUsers.add(new AdminUser(resultSet.getString("username")));
            }
        }
        return adminUsers;
    }
}


