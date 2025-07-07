package DAOs;

import Models.Achievement;

import java.sql.*;
import java.util.ArrayList;

public class AchievementDAO {

    public static void addAchievement(String name, String description, String imageUrl) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO achievements " +
                "(name, description, image_url) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imageUrl);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteAchievement(long id) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM achievements WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public static void updateAchievement(long id, String name, String description, String imageUrl) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE achievements SET " +
                "name = ?, description = ?, image_url = ? " +
                "WHERE id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imageUrl);
            preparedStatement.setLong(4, id);

            preparedStatement.executeUpdate();
        }
    }

    public static Achievement getAchievementByName(String name) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM achievements WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new Achievement(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("image_url")
                );
            }
        }
        return null;
    }

    public static ArrayList<Achievement> getAchievements() throws SQLException {
        ArrayList<Achievement> achievements = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM achievements")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                achievements.add(new Achievement(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("image_url")
                ));
            }
        }
        return achievements;
    }

}
