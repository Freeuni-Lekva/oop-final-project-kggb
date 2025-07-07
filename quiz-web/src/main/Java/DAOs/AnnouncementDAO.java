package DAOs;

import Models.Announcement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnnouncementDAO {

    public static void addAnnouncement(String type, String message, String title, String createdBy) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO announcements " +
                    "(type, message, title, created_by) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, createdBy);
            preparedStatement.executeUpdate();
        }
    }

    public static void removeAnnouncement(long id) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM announcements WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public static void updateAnnouncement(long id, String type, String message, String title, String createdBy) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE announcements SET " +
                "type = ?, message = ?, title = ?, created_by = ? WHERE id = ?")) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, createdBy);
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        }
    }

    public static ArrayList<Announcement> getAnnouncements() throws SQLException {
        ArrayList<Announcement> announcements = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                announcements.add(new Announcement(
                        resultSet.getLong("id"),
                        resultSet.getString("type"),
                        resultSet.getString("message"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getString("created_by"),
                        resultSet.getBoolean("active")
                ));
            }
        }
        return announcements;
    }
}
